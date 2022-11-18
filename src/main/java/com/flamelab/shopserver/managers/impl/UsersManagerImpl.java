package com.flamelab.shopserver.managers.impl;

import com.flamelab.shopserver.dtos.create.CreateUserDto;
import com.flamelab.shopserver.dtos.transafer.TransferUserDto;
import com.flamelab.shopserver.dtos.transafer.TransferWalletDto;
import com.flamelab.shopserver.dtos.update.UpdateUserDto;
import com.flamelab.shopserver.enums.ProductName;
import com.flamelab.shopserver.exceptions.ShopHasNotEnoughProductsException;
import com.flamelab.shopserver.exceptions.UserHasNotEnoughMoneyException;
import com.flamelab.shopserver.internal_data.Product;
import com.flamelab.shopserver.managers.UsersManager;
import com.flamelab.shopserver.services.ShopsService;
import com.flamelab.shopserver.services.UsersService;
import com.flamelab.shopserver.services.WalletsService;
import com.flamelab.shopserver.utiles.naming.FieldNames;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.flamelab.shopserver.enums.AmountActionType.DECREASE;
import static com.flamelab.shopserver.enums.AmountActionType.INCREASE;
import static com.flamelab.shopserver.enums.OwnerType.USER;

@Service
@RequiredArgsConstructor
public class UsersManagerImpl implements UsersManager {

    private final UsersService usersService;
    private final ShopsService shopsService;
    private final WalletsService walletsService;

    @Override
    public TransferUserDto createUser(CreateUserDto createUserDto) {
        TransferUserDto user = usersService.createUser(createUserDto);
        TransferWalletDto walletDto = walletsService.createWallet(user.getId(), USER);
        return usersService.addWalletToUser(user.getId(), walletDto.getId());
    }

    @Override
    public TransferUserDto getUserById(ObjectId userId) {
        return usersService.getUserById(userId);
    }

    @Override
    public Object getUserBy(Map<FieldNames, Object> criterias) {
        return usersService.getUserBy(criterias);
    }

    @Override
    public List<TransferUserDto> getAllUsers() {
        return usersService.getAllUsers();
    }

    @Override
    public TransferWalletDto getUserWallet(ObjectId userId) {
        return walletsService.getWalletByOwnerId(userId);
    }

    @Override
    public TransferUserDto updateUserData(ObjectId userId, UpdateUserDto updateUserDto) {
        return usersService.updateUserData(userId, updateUserDto);
    }

    @Override
    public TransferWalletDto deposit(ObjectId userId, int amount) {
        TransferUserDto user = usersService.getUserById(userId);
        return walletsService.updateWalletAmount(user.getId(), INCREASE, amount);
    }

    @Override
    public TransferUserDto buyProducts(ObjectId userId, ObjectId shopId, ProductName productName, int amount) {
        Product productDataFromShop = shopsService.getProductData(shopId, productName);
        if (productDataFromShop.getAmount() < amount) {
            throw new ShopHasNotEnoughProductsException(String.format("Shop with id '%s' has not enough amount of the product '%s'", shopId, productName));
        }
        double paymentMoney = productDataFromShop.getPrice() * amount;
        boolean isWalletHasEnoughAmount = walletsService.isWalletHasEnoughAmountByOwnerId(userId, paymentMoney);
        if (!isWalletHasEnoughAmount) {
            throw new UserHasNotEnoughMoneyException(String.format("User with id '%s' has not enough money for buy the '%s'", userId, productName));
        } else {
            walletsService.updateWalletAmount(userId, DECREASE, paymentMoney);
            walletsService.updateWalletAmount(shopId, INCREASE, paymentMoney);
            shopsService.decreaseProductsAmount(shopId, productName, amount);
            return usersService.addProductsToTheBucket(userId, productName, productDataFromShop.getPrice(), amount);
        }
    }

    @Override
    public void deleteUser(ObjectId userId) {
        TransferUserDto user = getUserById(userId);
        walletsService.deleteWalletById(user.getWalletId());
        usersService.deleteUser(userId);
    }
}
