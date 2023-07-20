package com.flamelab.shopserver.mappers;

import com.flamelab.shopserver.dtos.create.CreateShopDto;
import com.flamelab.shopserver.dtos.transfer.TransferShopDto;
import com.flamelab.shopserver.entities.Shop;
import com.flamelab.shopserver.entities.Wallet;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.utiles.RandomDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Component
@RequiredArgsConstructor
public class ShopMapper {

    private final RandomDataGenerator randomDataGenerator;

    public TransferShopDto mapToDto(Shop entity, Wallet wallet) {
        TransferShopDto dto = new TransferShopDto();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastUpdatedDate(entity.getLastUpdatedDate());
        dto.setName(entity.getName());
        dto.setWalletId(entity.getWalletId());
        dto.setOwnerId(entity.getOwnerId());
        dto.setOwnerName(entity.getOwnerName());
        dto.setWalletAmount(wallet.getAmount());
        return dto;
    }

    public List<TransferShopDto> mapToDtoList(List<Shop> entityList, List<Wallet> walletList) {
        return entityList.stream()
                .map(shop -> mapToDto(shop, getWalletByOwnerId(shop.getId(), walletList)))
                .collect(Collectors.toList());
    }

    public Shop mapToEntity(CreateShopDto createDto, String walletId, String userId, String username) {
        Shop entity = new Shop();
        entity.setId(randomDataGenerator.generateId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setLastUpdatedDate(LocalDateTime.now());
        entity.setName(createDto.getName());
        entity.setWalletId(walletId);
        entity.setOwnerId(userId);
        entity.setOwnerName(username);
        return entity;
    }

    private Wallet getWalletByOwnerId(String shopOwnerId, List<Wallet> walletList) {
        Optional<Wallet> walletByOwnerId = walletList.stream().filter(wallet -> wallet.getOwnerId().equals(shopOwnerId)).findFirst();
        if (walletByOwnerId.isPresent()) {
            return walletByOwnerId.get();
        } else {
            throw new ResourceException(NO_CONTENT, String.format("No existing wallet by shop with id: %s", shopOwnerId));
        }
    }

}
