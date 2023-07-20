package com.flamelab.shopserver.managers.impl;

import com.flamelab.shopserver.dtos.create.CreateTemporaryCodeDto;
import com.flamelab.shopserver.dtos.create.CreateUserDto;
import com.flamelab.shopserver.dtos.create.CreateWalletDto;
import com.flamelab.shopserver.dtos.transfer.TransferAuthTokenDto;
import com.flamelab.shopserver.dtos.transfer.TransferTemporaryCodeDto;
import com.flamelab.shopserver.dtos.transfer.TransferUserDto;
import com.flamelab.shopserver.dtos.transfer.TransferValidationResultDto;
import com.flamelab.shopserver.dtos.update.RecoverPasswordDto;
import com.flamelab.shopserver.dtos.update.UpdateUserDto;
import com.flamelab.shopserver.dtos.update.UpdateUserPasswordDto;
import com.flamelab.shopserver.entities.Shop;
import com.flamelab.shopserver.entities.User;
import com.flamelab.shopserver.entities.Wallet;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.managers.UsersManager;
import com.flamelab.shopserver.mappers.UsersMapper;
import com.flamelab.shopserver.services.*;
import com.flamelab.shopserver.utiles.EmailTextProvider;
import com.flamelab.shopserver.utiles.RandomDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.flamelab.shopserver.enums.Roles.ADMIN;
import static com.flamelab.shopserver.enums.Roles.MERCHANT;
import static com.flamelab.shopserver.enums.WalletOwnerTypes.ADMIN_OWNER;
import static com.flamelab.shopserver.enums.WalletOwnerTypes.USER_OWNER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Service
@RequiredArgsConstructor
public class UsersManagerImpl implements UsersManager {

    private final UsersService usersService;
    private final WalletsService walletsService;
    private final ShopsService shopsService;
    private final ProductsService productsService;
    private final UsersMapper usersMapper;
//    private final SendEmailService sendEmailService;
    private final EmailTextProvider emailTextProvider;
    private final TemporaryCodeService temporaryCodeService;
    private final RandomDataGenerator randomDataGenerator;
    private final int START_USER_MONEY = 0;

    @Override
    public TransferUserDto createUser(CreateUserDto createUserDto) {
        validateNewUserData(createUserDto);
        Wallet wallet = walletsService.createWallet(new CreateWalletDto(START_USER_MONEY));
        User user = usersService.createUser(createUserDto);
        usersService.addWalletToUser(user.getId(), wallet.getId());
        walletsService.setWalletOwner(wallet.getId(), USER_OWNER, user.getId(), user.getUsername());
        sendRegistrationTemporaryCodeToEmail(user.getEmail(), user.getId());
        return usersMapper.mapToDto(usersService.getUserById(user.getId()), walletsService.getWalletById(wallet.getId()));
    }

    @Override
    public TransferUserDto createUserAdmin(TransferAuthTokenDto authToken, CreateUserDto createUserDto) {
        TransferUserDto user = createUser(createUserDto);
        Wallet wallet = walletsService.getWalletByOwnerId(user.getId());
        walletsService.setWalletOwner(wallet.getId(), ADMIN_OWNER, user.getId(), user.getUsername());
        return user;
    }

    @Override
    public void sendTemporaryCodeToEmail(String email) {
        sendPasswordRecoveryTemporaryCodeToEmail(email);
    }

    @Override
    public TransferValidationResultDto verifyTempCode(TransferTemporaryCodeDto tempCodeDto) {
        if (temporaryCodeService.validateTempCode(tempCodeDto.getTempCode())) {
            TransferValidationResultDto tempCodeValidationResult = new TransferValidationResultDto(true);
            temporaryCodeService.deleteTemporaryCode(tempCodeDto.getTempCode());
            return tempCodeValidationResult;
        } else {
            throw new ResourceException(BAD_REQUEST, "Entered temporary code is not correct.");
        }
    }

    @Override
    public TransferUserDto confirmRegistration(String userId, int tempCode) {
        verifyTempCode(new TransferTemporaryCodeDto(tempCode));
        TransferUserDto userDto = usersMapper.mapToDto(usersService.activateUser(userId), walletsService.getWalletByOwnerId(userId));
        temporaryCodeService.deleteTemporaryCode(tempCode);
        return userDto;
    }

    @Override
    public TransferUserDto activateUser(TransferAuthTokenDto validateAuthToken, String userId) {
        return usersMapper.mapToDto(usersService.activateUser(userId), walletsService.getWalletByOwnerId(userId));
    }

    @Override
    public TransferUserDto getUserById(TransferAuthTokenDto authToken, String userId) {
        return usersMapper.mapToDto(usersService.getUserById(userId), walletsService.getWalletByOwnerId(userId));
    }

    @Override
    public List<TransferUserDto> getAllUsersByTextInParameters(TransferAuthTokenDto authToken, String text) {
        List<User> users = usersService.getAllUsersByTextInParameters(text);
        List<Wallet> wallets = walletsService.getWalletsByOwnerIds(users.stream().map(User::getId).collect(Collectors.toList()));
        return usersMapper.mapToDtoList(users, wallets);
    }

    @Override
    public List<TransferUserDto> getAllUsers(TransferAuthTokenDto authToken) {
        List<User> users = usersService.getAllUsers();
        List<Wallet> wallets = walletsService.getWalletsByOwnerIds(users.stream().map(User::getId).collect(Collectors.toList()));
        return usersMapper.mapToDtoList(users, wallets);
    }

    @Override
    public TransferUserDto updateUserData(TransferAuthTokenDto authToken, String userId, UpdateUserDto updateUserDto) {
        return usersMapper.mapToDto(usersService.updateUserData(userId, updateUserDto), walletsService.getWalletByOwnerId(userId));
    }

    @Override
    public void recoverPassword(RecoverPasswordDto recoverPasswordDto) {
        User user = usersService.getUserByEmail(recoverPasswordDto.getEmail());
        if (recoverPasswordDto.getNewPassword().equals(recoverPasswordDto.getRepeatNewPassword())) {
            usersService.recoverPassword(user.getId(), recoverPasswordDto.getNewPassword());
        } else {
            throw new ResourceException(BAD_REQUEST, "Passwords are not equals");
        }
    }

    @Override
    public void recoverPassword(TransferAuthTokenDto authToken, RecoverPasswordDto recoverPasswordDto) {
        if (recoverPasswordDto.getNewPassword().equals(recoverPasswordDto.getRepeatNewPassword())) {
            usersService.recoverPassword(authToken.getUserId(), recoverPasswordDto.getNewPassword());
        } else {
            throw new ResourceException(BAD_REQUEST, "Passwords are not equals");
        }
    }

    @Override
    public void updateUserPassword(TransferAuthTokenDto authToken, UpdateUserPasswordDto updateUserPasswordDto) {
        User user = usersService.getUserById(authToken.getUserId());
        if (!updateUserPasswordDto.getCurrentPassword().equals(user.getPassword())) {
            throw new ResourceException(BAD_REQUEST, "Wrong user's password");
        }
        if (!updateUserPasswordDto.getNewPassword().equals(updateUserPasswordDto.getRepeatNewPassword())) {
            throw new ResourceException(BAD_REQUEST, "New password are not equals");
        }
        usersService.updateUserPassword(user, updateUserPasswordDto);
    }

    @Override
    public void deleteUser(TransferAuthTokenDto authToken, String userId) {
        usersService.deleteUser(userId);
        if (authToken.getRole().equals(MERCHANT.name())) {
            List<String> shopIds = shopsService.getAllShopsByOwnerId(userId).stream().map(Shop::getId).toList();
            shopsService.deleteShops(shopIds);
            productsService.deleteProductsByShopIds(shopIds);
        }
        if (!authToken.getRole().equals(ADMIN.name())) {
            Wallet userWallet = walletsService.getWalletByOwnerId(userId);
            walletsService.deleteWallet(userWallet.getId());
        }
    }

    private void validateNewUserData(CreateUserDto createUserDto) {
        if (!createUserDto.getPassword().equals(createUserDto.getPasswordConfirmation())) {
            throw new ResourceException(BAD_REQUEST, "Passwords are not equals.");
        }
        if (usersService.isUserExistsByEmail(createUserDto.getEmail())) {
            throw new ResourceException(BAD_REQUEST, String.format("User with email '%s' already exists on the platform. \nPlease use another email.", createUserDto.getEmail()));
        }
    }

    private void sendRegistrationTemporaryCodeToEmail(String email, String userId) {
        CreateTemporaryCodeDto createTemporaryCodeDto = new CreateTemporaryCodeDto(email, randomDataGenerator.generateTemporaryCode());
        int tempCode = temporaryCodeService.generateTemporaryCode(createTemporaryCodeDto).getTempCode();
        if (usersService.isUserExistsByEmail(email)) {
//            sendEmailService.sendEmail(
//                    email,
//                    "Registration confirmation",
//                    emailTextProvider.provideConfirmRegistrationText(userId, tempCode));
        } else {
            throw new ResourceException(NO_CONTENT, String.format("User with email '%s' does not exists", email));
        }
    }

    private void sendPasswordRecoveryTemporaryCodeToEmail(String email) {
        CreateTemporaryCodeDto createTemporaryCodeDto = new CreateTemporaryCodeDto(email, randomDataGenerator.generateTemporaryCode());
        int tempCode = temporaryCodeService.generateTemporaryCode(createTemporaryCodeDto).getTempCode();
        if (usersService.isUserExistsByEmail(email)) {
//            sendEmailService.sendEmail(
//                    email,
//                    "Password Recovery",
//                    emailTextProvider.providePasswordRecoverySendTempCodeText(tempCode));
        } else {
            throw new ResourceException(NO_CONTENT, String.format("User with email '%s' does not exists", email));
        }
    }

}
