package com.flamelab.shopserver.services.impl;

import com.flamelab.shopserver.dtos.create.CreateProductDto;
import com.flamelab.shopserver.entities.Product;
import com.flamelab.shopserver.enums.NumberActionType;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.mappers.ProductMapper;
import com.flamelab.shopserver.repositories.ProductsRepository;
import com.flamelab.shopserver.services.ProductsService;
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
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductMapper productMapper;

    @Override
    public Product createProduct(CreateProductDto createProductDto) {
        return productsRepository.save(productMapper.mapToEntity(createProductDto));
    }

    @Override
    public Product getProductById(String productId) {
        Optional<Product> optionalProduct = productsRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new ResourceException(NO_CONTENT, String.format("There is no product with id '%s'", productId));
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    public List<Product> searchAllProductsByText(String text) {
        return productsRepository.findAllByNameContaining(text);
    }

    @Override
    public List<Product> getAllProductsByShopId(String shopId) {
        return productsRepository.findByOwnerShopId(shopId);
    }

    @Override
    public boolean isEnoughAmountOfProducts(String productId, int requestedAmount) {
        Product product = getProductById(productId);
        return product.getAmount() >= requestedAmount;
    }

    @Override
    public Product setProductPrice(String productId, double newPrice) {
        Product product = getProductById(productId);
        product.setPrice(newPrice);
        return productsRepository.save(product);
    }

    @Override
    public Product renameProduct(String productId, String newName) {
        Product product = getProductById(productId);
        product.setName(newName);
        return productsRepository.save(product);
    }

    @Override
    public Product updateProductAmount(String productId, NumberActionType actionType, int newAmount) {
        Product product = getProductById(productId);
        int resultAmount = product.getAmount();
        if (actionType.equals(CHANGE)) {
            resultAmount = newAmount;
        } else if (actionType.equals(INCREASE)) {
            resultAmount += newAmount;
        } else if (actionType.equals(DECREASE)) {
            resultAmount -= newAmount;
        } else {
            resultAmount = newAmount;
        }
        product.setAmount(resultAmount);
        return productsRepository.save(product);
    }

    @Override
    public void deleteProducts(List<String> productIds) {
        productsRepository.deleteAllById(productIds);
    }

    @Override
    public void deleteProductsByShopIds(List<String> shopIds) {
        List<List<String>> productIdsByEachShop = shopIds.stream()
                .map(productsRepository::findAllByOwnerShopId)
                .map(products -> products.stream().map(Product::getId).collect(Collectors.toList()))
                .toList();
        List<String> allProductIds = new ArrayList<>();
        productIdsByEachShop.forEach(allProductIds::addAll);
        deleteProducts(allProductIds);
    }

}
