package com.flamelab.shopserver.managers.impl;

import com.flamelab.shopserver.dtos.create.CreateProductDto;
import com.flamelab.shopserver.dtos.create.CreatePurchaseOperationDto;
import com.flamelab.shopserver.dtos.create.CreateShopDto;
import com.flamelab.shopserver.dtos.create.CreateWalletDto;
import com.flamelab.shopserver.dtos.transfer.TransferAuthTokenDto;
import com.flamelab.shopserver.dtos.transfer.TransferProductDto;
import com.flamelab.shopserver.dtos.transfer.TransferShopDto;
import com.flamelab.shopserver.entities.Product;
import com.flamelab.shopserver.entities.Shop;
import com.flamelab.shopserver.entities.User;
import com.flamelab.shopserver.entities.Wallet;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.managers.ShopsManager;
import com.flamelab.shopserver.mappers.ProductMapper;
import com.flamelab.shopserver.mappers.ShopMapper;
import com.flamelab.shopserver.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.flamelab.shopserver.enums.NumberActionType.*;
import static com.flamelab.shopserver.enums.WalletOwnerTypes.SHOP;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ShopsManagerImpl implements ShopsManager {

    private final ShopsService shopsService;
    private final WalletsService walletsService;
    private final UsersService usersService;
    private final ProductsService productsService;
    private final PurchaseOperationsService purchaseOperationsService;
    private final ShopMapper shopMapper;
    private final ProductMapper productMapper;
    private final int START_SHOP_MONEY = 1000;

    @Override
    public TransferShopDto createShop(TransferAuthTokenDto authToken, CreateShopDto createShopDto) {
        verifyNewShopData(createShopDto);
        Wallet wallet = walletsService.createWallet(new CreateWalletDto(START_SHOP_MONEY));
        Shop shop = shopsService.createShop(createShopDto, wallet.getId(), authToken.getUserId());
        walletsService.setWalletOwner(wallet.getId(), SHOP, shop.getId(), shop.getName());
        List<Shop> allShopsByOwnerId = shopsService.getAllShopsByOwnerId(authToken.getUserId());
        walletsService.getAllWalletsByShopsIds(allShopsByOwnerId.stream().map(Shop::getId).collect(Collectors.toList()));
        return shopMapper.mapToDto(shopsService.getShopById(shop.getId()), walletsService.getWalletById(wallet.getId()));
    }

    @Override
    public TransferShopDto getShopById(TransferAuthTokenDto authToken, String shopId) {
        return shopMapper.mapToDto(shopsService.getShopById(shopId), walletsService.getWalletByOwnerId(authToken.getUserId()));
    }

    @Override
    public List<TransferShopDto> getAllShops(TransferAuthTokenDto authToken) {
        List<Shop> allShopsByOwnerId = shopsService.getAllShops();
        List<Wallet> allWalletsByShopsIds = walletsService.getAllWalletsByShopsIds(allShopsByOwnerId.stream().map(Shop::getId).collect(Collectors.toList()));
        return shopMapper.mapToDtoList(shopsService.getAllShops(), allWalletsByShopsIds);
    }

    @Override
    public List<TransferShopDto> getAllShopsByOwnerId(TransferAuthTokenDto authToken, String ownerId) {
        List<Shop> allShopsByOwnerId = shopsService.getAllShopsByOwnerId(ownerId);
        List<Wallet> allWalletsByShopsIds = walletsService.getAllWalletsByShopsIds(allShopsByOwnerId.stream().map(Shop::getId).collect(Collectors.toList()));
        return shopMapper.mapToDtoList(allShopsByOwnerId, allWalletsByShopsIds);
    }

    @Override
    public List<TransferShopDto> getAllShopsByTextInParameters(TransferAuthTokenDto authToken, String text) {
        List<Shop> allShopsByOwnerId = shopsService.getAllShopsByOwnerId(authToken.getUserId());
        List<Wallet> allWalletsByShopsIds = walletsService.getAllWalletsByShopsIds(allShopsByOwnerId.stream().map(Shop::getId).collect(Collectors.toList()));
        return shopMapper.mapToDtoList(shopsService.getAllShopsByTextInName(text), allWalletsByShopsIds);
    }

    @Override
    public List<TransferProductDto> getAllProductsByTextInParameters(TransferAuthTokenDto authToken, String text) {
        return productMapper.mapToDtoList(productsService.searchAllProductsByText(text));
    }

    @Override
    public List<TransferProductDto> getAllProductsInTheShop(TransferAuthTokenDto authToken, String shopId) {
        return productMapper.mapToDtoList(productsService.getAllProductsByShopId(shopId));
    }

    @Override
    public TransferShopDto renameShop(TransferAuthTokenDto authToken, String shopId, String newName) {
        Wallet walletByOwnerId = walletsService.getWalletByOwnerId(authToken.getUserId());
        return shopMapper.mapToDto(shopsService.renameShop(shopId, newName), walletByOwnerId);
    }

    @Override
    public TransferProductDto renameProduct(TransferAuthTokenDto authToken, String productId, String newName) {
        return productMapper.mapToDto(productsService.renameProduct(productId, newName));
    }

    @Override
    public TransferProductDto buyNewProductsShopFromTheStock(TransferAuthTokenDto authToken, String shopId, String productName, int productAmount, double price) {
        Wallet shopWallet = walletsService.getWalletByOwnerId(shopId);
        Shop shop = shopsService.getShopById(shopId);
        double finalPrice = productAmount * price;
        if (walletsService.isWalletHasEnoughAmountForPurchase(shopWallet.getId(), finalPrice)) {
            Product product = productsService.createProduct(new CreateProductDto(shopId, productName, productAmount, price));
            walletsService.updateWalletAmount(shopWallet.getId(), DECREASE, finalPrice);
            purchaseOperationsService.createPurchaseOperation(
                    new CreatePurchaseOperationDto(productName, productAmount, finalPrice, "22222222-2222-2222-2222-222222222222", "Stock", shopId, shop.getName()));
            return productMapper.mapToDto(product);
        } else {
            throw new ResourceException(BAD_REQUEST, String.format("Shop with name '%s' has not enough money for making this purchase.", shop.getName()));
        }
    }

    @Override
    public TransferProductDto buyExistsProductsShopFromTheStock(TransferAuthTokenDto authToken, String shopId, String productId, double productCost, int productAmount) {
        Wallet shopWallet = walletsService.getWalletByOwnerId(shopId);
        Shop shop = shopsService.getShopById(shopId);
        double finalPrice = productAmount * productCost;
        if (walletsService.isWalletHasEnoughAmountForPurchase(shopWallet.getId(), finalPrice)) {
            productsService.updateProductAmount(productId, INCREASE, productAmount);
            Product product = productsService.setProductPrice(productId, productCost);
            walletsService.updateWalletAmount(shopWallet.getId(), DECREASE, finalPrice);
            purchaseOperationsService.createPurchaseOperation(
                    new CreatePurchaseOperationDto(product.getName(), productAmount, finalPrice, "22222222-2222-2222-2222-222222222222", "Stock", shopId, shop.getName()));
            return productMapper.mapToDto(product);
        } else {
            throw new ResourceException(BAD_REQUEST, String.format("Shop with name '%s' has not enough money for making this purchase.", shop.getName()));
        }
    }

    @Override
    public TransferProductDto buyProductsUserFromTheShop(TransferAuthTokenDto authToken, String shopId, String productId, int productAmount) {
        Shop shop = shopsService.getShopById(shopId);
        User user = usersService.getUserById(authToken.getUserId());
        Product product = productsService.getProductById(productId);
        Wallet shopWallet = walletsService.getWalletByOwnerId(shopId);
        Wallet userWallet = walletsService.getWalletByOwnerId(authToken.getUserId());
        double finalPrice = product.getPrice() * productAmount;
        if (productsService.isEnoughAmountOfProducts(product.getId(), productAmount)) {
            if (walletsService.isWalletHasEnoughAmountForPurchase(userWallet.getId(), finalPrice)) {
                product = productsService.updateProductAmount(product.getId(), DECREASE, productAmount);
                walletsService.updateWalletAmount(userWallet.getId(), DECREASE, finalPrice);
                walletsService.updateWalletAmount(shopWallet.getId(), INCREASE, finalPrice);
                purchaseOperationsService.createPurchaseOperation(
                        new CreatePurchaseOperationDto(product.getName(), productAmount, finalPrice, shopId, shop.getName(), authToken.getUserId(), user.getUsername()));
                return productMapper.mapToDto(product);
            } else {
                throw new ResourceException(BAD_REQUEST, String.format("User with name '%s' has not enough amount in the wallet", user.getUsername()));
            }
        } else {
            throw new ResourceException(BAD_REQUEST, String.format("Product with name '%s' has not enough amount in the shop with name '%s'", product.getName(), shop.getName()));
        }
    }

    @Override
    public TransferProductDto setProductPrice(TransferAuthTokenDto authToken, String productId, double newPrice) {
        return productMapper.mapToDto(productsService.setProductPrice(productId, newPrice));
    }

    @Override
    public TransferProductDto setProductAmount(TransferAuthTokenDto validateAuthToken, String productId, int newAmount) {
        return productMapper.mapToDto(productsService.updateProductAmount(productId, CHANGE, newAmount));
    }

    @Override
    public TransferProductDto deleteProductAmount(TransferAuthTokenDto validateAuthToken, String productId, int amount) {
        return productMapper.mapToDto(productsService.updateProductAmount(productId, DECREASE, amount));
    }

    @Override
    public void deleteProduct(TransferAuthTokenDto validateAuthToken, String productId) {
        productsService.deleteProducts(Collections.singletonList(productId));
    }

    @Override
    public void deleteShop(TransferAuthTokenDto authToken, String shopId) {
        shopsService.deleteShop(shopId);
        List<String> productsIds = productsService.getAllProductsByShopId(shopId).stream().map(Product::getId).toList();
        productsService.deleteProducts(productsIds);
        Wallet shopWallet = walletsService.getWalletByOwnerId(shopId);
        walletsService.deleteWallet(shopWallet.getId());
    }

    private void verifyNewShopData(CreateShopDto createShopDto) {
        if (shopsService.isShopExistsWithName(createShopDto.getName())) {
            throw new ResourceException(BAD_REQUEST, String.format("Shop with name %s already exists.", createShopDto.getName()));
        }
    }

}
