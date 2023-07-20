package com.flamelab.shopserver.services;

import com.flamelab.shopserver.dtos.create.CreatePurchaseOperationDto;
import com.flamelab.shopserver.entities.PurchaseOperation;

import java.util.List;

public interface PurchaseOperationsService {

    void createPurchaseOperation(CreatePurchaseOperationDto createPurchaseOperationDto);

    PurchaseOperation getPurchaseOperationById(String operationId);

    List<PurchaseOperation> getAllPurchaseOperationsByUser(String userId);

    List<PurchaseOperation> getAllPurchaseOperationsByShop(String shopId);

    List<PurchaseOperation> getAllPurchaseOperationsByShopAndContainsTextInProductName(String shopId, String productName);

}
