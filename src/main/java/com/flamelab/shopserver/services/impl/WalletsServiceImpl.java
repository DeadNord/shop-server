package com.flamelab.shopserver.services.impl;

import com.flamelab.shopserver.dtos.create.CreateWalletDto;
import com.flamelab.shopserver.entities.Wallet;
import com.flamelab.shopserver.enums.NumberActionType;
import com.flamelab.shopserver.enums.WalletOwnerTypes;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.mappers.WalletMapper;
import com.flamelab.shopserver.repositories.WalletsRepository;
import com.flamelab.shopserver.services.WalletsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.flamelab.shopserver.enums.NumberActionType.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Service
@RequiredArgsConstructor
public class WalletsServiceImpl implements WalletsService {

    private final WalletsRepository walletsRepository;
    private final WalletMapper walletMapper;

    @Override
    public Wallet createWallet(CreateWalletDto createWalletDto) {
        // when a new wallet saves to the repository it saves with ownerId = null and ownerType = null
        // ownerId and ownerType saves into the wallet entity in methods of creation new user and new shop
        return walletsRepository.save(walletMapper.mapToEntity(createWalletDto));
    }

    @Override
    public Wallet getWalletById(String walletId) {
        Optional<Wallet> optionalWallet = walletsRepository.findById(walletId);
        if (optionalWallet.isPresent()) {
            return optionalWallet.get();
        } else {
            throw new ResourceException(NO_CONTENT, String.format("Wallet with id '%s' does not exists", walletId));
        }
    }

    @Override
    public Wallet getWalletByOwnerId(String ownerId) {
        Optional<Wallet> optionalWallet = walletsRepository.findByOwnerId(ownerId);
        if (optionalWallet.isPresent()) {
            return optionalWallet.get();
        } else {
            throw new ResourceException(NO_CONTENT, String.format("Wallet with ownerId '%s' does not exists", ownerId));
        }
    }

    @Override
    public List<Wallet> getWalletsByOwnerIds(List<String> ownerIds) {
        List<Wallet> wallets = new ArrayList<>();
        ownerIds.forEach(id -> wallets.add(getWalletByOwnerId(id)));
        return wallets;
    }

    @Override
    public List<Wallet> getAllWallets() {
        return walletsRepository.findAll();
    }

    @Override
    public List<Wallet> getAllWalletsByShopsIds(List<String> shopIds) {
        return shopIds.stream()
                .map(this::getWalletByOwnerId)
                .collect(Collectors.toList());
    }

    @Override
    public Wallet updateWalletAmount(String walletId, NumberActionType actionType, double newAmount) {
        Wallet wallet = getWalletById(walletId);
        double resultAmount = wallet.getAmount();
        if (actionType.equals(CHANGE)) {
            resultAmount = newAmount;
        } else if (actionType.equals(INCREASE)) {
            resultAmount = wallet.getAmount() + newAmount;
        } else if (actionType.equals(DECREASE)) {
            resultAmount = wallet.getAmount() - newAmount;
        }
        wallet.setAmount(resultAmount);
        return walletsRepository.save(wallet);
    }

    @Override
    public void setWalletOwner(String walletId, WalletOwnerTypes ownerType, String ownerId, String ownerName) {
        Wallet wallet = getWalletById(walletId);
        wallet.setOwnerType(ownerType.name());
        wallet.setOwnerId(ownerId);
        wallet.setOwnerName(ownerName);
        walletsRepository.save(wallet);
    }

    @Override
    public boolean isWalletHasEnoughAmountForPurchase(String walletId, double purchasePrice) {
        return getWalletById(walletId).getAmount() >= purchasePrice;
    }

    @Override
    public void deleteWallet(String walletId) {
        walletsRepository.deleteById(walletId);
    }

}
