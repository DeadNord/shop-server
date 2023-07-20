package com.flamelab.shopserver.repositories;

import com.flamelab.shopserver.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, String> {

    List<Product> findByOwnerShopId(String ownerShopId);

    Optional<Product> findByOwnerShopIdAndName(String ownerShopId, String name);

    List<Product> findAllByOwnerShopId(String ownerShopId);

    List<Product> findAllByNameContaining(String name);

}
