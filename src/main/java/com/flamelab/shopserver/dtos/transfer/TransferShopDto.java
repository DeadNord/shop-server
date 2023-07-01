package com.flamelab.shopserver.dtos.transfer;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransferShopDto extends TransferCommonDto {

    private String name;
    private String walletId;
    private String ownerId;
    private double walletAmount;

}
