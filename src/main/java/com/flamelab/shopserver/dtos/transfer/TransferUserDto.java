package com.flamelab.shopserver.dtos.transfer;

import com.flamelab.shopserver.enums.Roles;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransferUserDto extends TransferCommonDto {

    private String username;
    private String email;
    private String walletId;
    private double walletAmount;
    private Roles role;
    private boolean isActive;

}
