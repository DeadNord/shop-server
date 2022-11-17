package com.flamelab.shopserver.managers;

import com.flamelab.shopserver.dtos.create.CreateUserDto;
import com.flamelab.shopserver.dtos.transafer.TransferUserDto;
import com.flamelab.shopserver.dtos.transafer.TransferWalletDto;
import com.flamelab.shopserver.dtos.update.UpdateUserDto;
import com.flamelab.shopserver.enums.ProductName;
import org.bson.types.ObjectId;

import java.util.List;

public interface UsersManager {

    TransferUserDto createUser(CreateUserDto createUserDto);

    TransferUserDto getUserById(ObjectId userId);

    List<TransferUserDto> getAllUsers();

    TransferWalletDto getUserWallet(ObjectId userId);

    TransferUserDto updateUserData(ObjectId userId, UpdateUserDto updateUserDto);

    TransferWalletDto deposit(ObjectId userId, int amount);

    TransferUserDto buyProducts(ObjectId userId, ObjectId shopId, ProductName productName, int amount);

    void deleteUser(ObjectId userId);
}
