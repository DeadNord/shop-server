package com.flamelab.shopserver.services;

import com.flamelab.shopserver.dtos.create.CreateWalletDto;
import com.flamelab.shopserver.entities.Wallet;
import com.flamelab.shopserver.enums.NumberActionType;
import com.flamelab.shopserver.enums.WalletOwnerTypes;

import java.util.List;

public interface WalletsService {

    Wallet createWallet(CreateWalletDto createWalletDto);

    Wallet getWalletById(String walletId);

    Wallet getWalletByOwnerId(String ownerId);

    List<Wallet> getWalletsByOwnerIds(List<String> ownerIds);

    List<Wallet> getAllWallets();

    List<Wallet> getAllWalletsByShopsIds(List<String> shopIds);

    Wallet updateWalletAmount(String walletId, NumberActionType actionType, double amount);

    void setWalletOwner(String walletId, WalletOwnerTypes ownerType, String ownerId, String ownerName);

    boolean isWalletHasEnoughAmountForPurchase(String walletId, double purchasePrice);

    void deleteWallet(String walletId);
}
