package com.flamelab.shopserver.services;

import com.flamelab.shopserver.dtos.create.CreateProductDto;
import com.flamelab.shopserver.entities.Product;
import com.flamelab.shopserver.enums.NumberActionType;

import java.util.List;

public interface ProductsService {

    Product createProduct(CreateProductDto createProductDto);

    Product getProductById(String productId);

    List<Product> getAllProducts();

    List<Product> searchAllProductsByText(String text);

    List<Product> getAllProductsByShopId(String shopId);

    boolean isEnoughAmountOfProducts(String productId, int requestedAmount);

    Product setProductPrice(String productId, double newPrice);

    Product renameProduct(String shopId, String newName);

    Product updateProductAmount(String productId, NumberActionType actionType, int newAmount);

    void deleteProducts(List<String> productIds);

    void deleteProductsByShopIds(List<String> shopIds);
}
