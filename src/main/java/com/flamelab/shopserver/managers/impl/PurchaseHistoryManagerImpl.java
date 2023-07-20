package com.flamelab.shopserver.managers.impl;

import com.flamelab.shopserver.dtos.transfer.TransferAuthTokenDto;
import com.flamelab.shopserver.dtos.transfer.TransferPurchaseOperationDto;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.managers.PurchaseHistoryManager;
import com.flamelab.shopserver.mappers.PurchaseOperationMapper;
import com.flamelab.shopserver.services.ProductsService;
import com.flamelab.shopserver.services.PurchaseOperationsService;
import com.flamelab.shopserver.services.ShopsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.flamelab.shopserver.enums.Roles.ADMIN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryManagerImpl implements PurchaseHistoryManager {

    private final ShopsService shopsService;
    private final PurchaseOperationsService purchaseOperationsService;
    private final PurchaseOperationMapper purchaseOperationMapper;
    private final ProductsService productsService;

    @Override
    public List<TransferPurchaseOperationDto> getPurchaseHistoryForUser(TransferAuthTokenDto authToken, String userId) {
        if (authToken.getRole().equals(ADMIN)) {
            return purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByUser(userId));
        } else {
            return purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByUser(authToken.getUserId()));
        }
    }

    @Override
    public List<TransferPurchaseOperationDto> getPurchaseHistoryForShop(TransferAuthTokenDto authToken, String shopId) {
        if (authToken.getRole().equals(ADMIN)) {
            return purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByShop(shopId));
        } else {
            if (shopsService.isUserOwnerOfShop(authToken.getUserId(), shopId)) {
                return purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByShop(shopId));
            } else {
                throw new ResourceException(UNAUTHORIZED, "User is not owner of the shop.");
            }
        }
    }

    @Override
    public List<TransferPurchaseOperationDto> getPurchaseHistoryForShopByProductId(TransferAuthTokenDto authToken, String shopId, String productId) {
        List<TransferPurchaseOperationDto> resultPurchaseOperations = new ArrayList<>();
        if (authToken.getRole().equals(ADMIN)) {
            resultPurchaseOperations.addAll(purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByShop(shopId)));
        } else {
            if (shopsService.isUserOwnerOfShop(authToken.getUserId(), shopId)) {
                resultPurchaseOperations.addAll(purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByShop(shopId)));
            } else {
                throw new ResourceException(UNAUTHORIZED, "User is not owner of the shop.");
            }
        }
        return resultPurchaseOperations.stream()
                .filter(operation -> operation.getProductName().equals(productsService.getProductById(productId).getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferPurchaseOperationDto> getPurchaseHistoryForShopByProductName(TransferAuthTokenDto authToken, String shopId, String productName) {
        List<TransferPurchaseOperationDto> resultPurchaseOperations = new ArrayList<>();
        if (authToken.getRole().equals(ADMIN)) {
            resultPurchaseOperations.addAll(purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByShopAndContainsTextInProductName(shopId, productName)));
        } else {
            if (shopsService.isUserOwnerOfShop(authToken.getUserId(), shopId)) {
                resultPurchaseOperations.addAll(purchaseOperationMapper.mapToDtoList(purchaseOperationsService.getAllPurchaseOperationsByShopAndContainsTextInProductName(shopId, productName)));
            } else {
                throw new ResourceException(UNAUTHORIZED, "User is not owner of the shop.");
            }
        }
        return resultPurchaseOperations;
    }

}
