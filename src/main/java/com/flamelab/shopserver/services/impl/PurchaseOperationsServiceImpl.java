package com.flamelab.shopserver.services.impl;

import com.flamelab.shopserver.dtos.create.CreatePurchaseOperationDto;
import com.flamelab.shopserver.entities.PurchaseOperation;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.mappers.PurchaseOperationMapper;
import com.flamelab.shopserver.repositories.PurchaseOperationsRepository;
import com.flamelab.shopserver.services.PurchaseOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Service
@RequiredArgsConstructor
public class PurchaseOperationsServiceImpl implements PurchaseOperationsService {

    private final PurchaseOperationsRepository purchaseOperationsRepository;
    private final PurchaseOperationMapper purchaseOperationMapper;

    @Override
    public void createPurchaseOperation(CreatePurchaseOperationDto createPurchaseOperationDto) {
        purchaseOperationsRepository.save(purchaseOperationMapper.mapToEntity(createPurchaseOperationDto));
    }

    @Override
    public PurchaseOperation getPurchaseOperationById(String operationId) {
        Optional<PurchaseOperation> optionalPurchaseOperation = purchaseOperationsRepository.findById(operationId);
        if (optionalPurchaseOperation.isPresent()) {
            return optionalPurchaseOperation.get();
        } else {
            throw new ResourceException(NO_CONTENT, String.format("Purchase operation with id '%s' does not exists", operationId));
        }
    }

    @Override
    public List<PurchaseOperation> getAllPurchaseOperationsByUser(String userId) {
        return purchaseOperationsRepository.findAllByCustomerId(userId).stream()
                .sorted(Comparator.comparing(PurchaseOperation::getCreatedDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOperation> getAllPurchaseOperationsByShop(String shopId) {
        List<PurchaseOperation> result = new ArrayList<>();
        result.addAll(purchaseOperationsRepository.findAllByMerchantId(shopId));
        result.addAll(purchaseOperationsRepository.findAllByCustomerId(shopId));
        return result.stream()
                .sorted(Comparator.comparing(PurchaseOperation::getCreatedDate))
                .toList();
    }

    @Override
    public List<PurchaseOperation> getAllPurchaseOperationsByShopAndContainsTextInProductName(String shopId, String productName) {
        return purchaseOperationsRepository.findAllByProductNameContaining(productName);
    }


}
