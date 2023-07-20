package com.flamelab.shopserver.managers;

import com.flamelab.shopserver.dtos.create.CreateShopDto;
import com.flamelab.shopserver.dtos.transfer.TransferAuthTokenDto;
import com.flamelab.shopserver.dtos.transfer.TransferProductDto;
import com.flamelab.shopserver.dtos.transfer.TransferShopDto;

import java.util.List;

public interface ShopsManager {

    TransferShopDto createShop(TransferAuthTokenDto authToken, CreateShopDto createShopDto);

    TransferShopDto getShopById(TransferAuthTokenDto authToken, String shopId);

    List<TransferShopDto> getAllShops(TransferAuthTokenDto authToken);

    List<TransferShopDto> getAllShopsByOwnerId(TransferAuthTokenDto authToken, String ownerId);

    List<TransferShopDto> getAllShopsByTextInParameters(TransferAuthTokenDto authToken, String text);

    List<TransferProductDto> getAllProductsByTextInParameters(TransferAuthTokenDto authToken, String text);

    List<TransferProductDto> getAllProductsInTheShop(TransferAuthTokenDto authToken, String shopId);

    TransferShopDto renameShop(TransferAuthTokenDto authToken, String shopId, String newName);

    TransferProductDto renameProduct(TransferAuthTokenDto authToken, String shopId, String newName);

    TransferProductDto buyNewProductsShopFromTheStock(TransferAuthTokenDto authToken, String shopId, String productName, int productAmount, double price);

    TransferProductDto buyExistsProductsShopFromTheStock(TransferAuthTokenDto authToken, String shopId, String productId, double productCost, int amount);

    TransferProductDto buyProductsUserFromTheShop(TransferAuthTokenDto authToken, String shopId, String productName, int productAmount);

    TransferProductDto setProductPrice(TransferAuthTokenDto authToken, String productId, double newPrice);

    TransferProductDto setProductAmount(TransferAuthTokenDto validateAuthToken, String productId, int newAmount);

    TransferProductDto deleteProductAmount(TransferAuthTokenDto validateAuthToken, String productId, int amount);

    void deleteProduct(TransferAuthTokenDto validateAuthToken, String productId);

    void deleteShop(TransferAuthTokenDto authToken, String shopId);
}
