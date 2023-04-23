package com.flamelab.shopserver.services.impl;

import com.flamelab.shopserver.dtos.create.CreateShopDto;
import com.flamelab.shopserver.entities.Shop;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.mappers.ShopMapper;
import com.flamelab.shopserver.repositories.ShopsRepository;
import com.flamelab.shopserver.services.ShopsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Service
@RequiredArgsConstructor
public class ShopsServiceImpl implements ShopsService {

    private final ShopsRepository shopsRepository;
    private final ShopMapper shopMapper;

    @Override
    public Shop createShop(CreateShopDto createShopDto, String walletId, String userId) {
        return shopsRepository.save(shopMapper.mapToEntity(createShopDto, walletId, userId));
    }

    @Override
    public Shop getShopById(String shopId) {
        Optional<Shop> optionalShop = shopsRepository.findById(shopId);
        if (optionalShop.isPresent()) {
            return optionalShop.get();
        } else {
            throw new ResourceException(NO_CONTENT, String.format("Shop with id '%s' does not exists.", shopId));
        }
    }

    @Override
    public List<Shop> getAllShops() {
        return shopsRepository.findAll();
    }

    @Override
    public List<Shop> getAllShopsByOwnerId(String ownerId) {
        return shopsRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public List<Shop> getAllShopsByTextInName(String text) {
        return shopsRepository.findAllByNameContaining(text);
    }

    @Override
    public Shop renameShop(String shopId, String newName) {
        Shop shop = getShopById(shopId);
        shop.setName(newName);
        return shopsRepository.save(shop);
    }

    @Override
    public void deleteShop(String shopId) {
        shopsRepository.deleteById(shopId);
    }

    @Override
    public void deleteShops(List<String> shopIds) {
        shopsRepository.deleteAllById(shopIds);
    }

}
