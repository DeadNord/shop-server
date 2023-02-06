package com.flamelab.shopserver.dtos.create;

import com.flamelab.shopserver.enums.Roles;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CreateUserDto extends CommonCreateDto {

    private String name;
    private String email;
    private String password;
    private Roles role;

}
