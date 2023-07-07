package com.flamelab.shopserver.managers;

import com.flamelab.shopserver.dtos.transfer.TransferAuthTokenDto;
import com.flamelab.shopserver.dtos.transfer.TransferPurchaseOperationDto;

import java.util.List;

public interface PurchaseHistoryManager {

    List<TransferPurchaseOperationDto> getPurchaseHistoryForUser(TransferAuthTokenDto authToken, String userId);

    List<TransferPurchaseOperationDto> getPurchaseHistoryForShop(TransferAuthTokenDto authToken, String shopId);

    List<TransferPurchaseOperationDto> getPurchaseHistoryForShopByProductId(TransferAuthTokenDto authToken, String shopId, String productId);

}
