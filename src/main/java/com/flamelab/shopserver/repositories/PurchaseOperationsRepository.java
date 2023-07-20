package com.flamelab.shopserver.repositories;

import com.flamelab.shopserver.entities.PurchaseOperation;
import com.flamelab.shopserver.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseOperationsRepository extends JpaRepository<PurchaseOperation, String> {

    List<PurchaseOperation> findAllByCustomerId(String customerId);

    List<PurchaseOperation> findAllByMerchantId(String merchantId);

    List<PurchaseOperation> findAllByProductNameContaining(String productName);

}
