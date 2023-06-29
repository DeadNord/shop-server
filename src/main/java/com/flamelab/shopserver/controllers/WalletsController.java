package com.flamelab.shopserver.controllers;

import com.flamelab.shopserver.dtos.create.wallet_operations.CreateShopDepositData;
import com.flamelab.shopserver.dtos.create.wallet_operations.CreateShopWithdrawData;
import com.flamelab.shopserver.dtos.create.wallet_operations.CreateUserDepositData;
import com.flamelab.shopserver.dtos.create.wallet_operations.CreateUserWithdrawData;
import com.flamelab.shopserver.enums.Roles;
import com.flamelab.shopserver.managers.AuthManager;
import com.flamelab.shopserver.managers.WalletsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletsController extends BaseController {

    private final AuthManager authManager;
    private final WalletsManager walletsManager;

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@RequestHeader("Authorization") String authorization, @PathVariable("id") String walletId) {
        return ResponseEntity
                .status(OK)
                .body(walletsManager.getWalletById(
                        authManager.validateAuthToken(authorization, Roles.ADMIN_CUSTOMER_MERCHANT()),
                        walletId));
    }

    @GetMapping
    public ResponseEntity<?> getAllWallets(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity
                .status(OK)
                .body(walletsManager.getAllWallets(authManager.validateAuthToken(authorization, Roles.ADMIN())));
    }

    @PutMapping("/deposit/user")
    public ResponseEntity<?> doDepositToUsersWallet(@RequestHeader("Authorization") String authorization, @RequestBody CreateUserDepositData createUserDepositData) {
        return ResponseEntity
                .status(OK)
                .body(walletsManager.doDepositToUsersWallet(
                        authManager.validateAuthToken(authorization, Roles.MERCHANT_CUSTOMER()),
                        createUserDepositData));
    }

    @PutMapping("/deposit/shop")
    public ResponseEntity<?> doDepositToShopsWallet(@RequestHeader("Authorization") String authorization, @RequestBody CreateShopDepositData createShopDepositData) {
        return ResponseEntity
                .status(OK)
                .body(walletsManager.doDepositToShopWallet(
                        authManager.validateAuthToken(authorization, Roles.MERCHANT_CUSTOMER()),
                        createShopDepositData));
    }

    @PutMapping("/withdraw/user")
    public ResponseEntity<?> doWithdrawFromUserWallet(@RequestHeader("Authorization") String authorization, @RequestBody CreateUserWithdrawData createWalletDepositData) {
        return ResponseEntity
                .status(OK)
                .body(walletsManager.doWithdrawFromUsersWallet
                        (authManager.validateAuthToken(authorization, Roles.MERCHANT_CUSTOMER()),
                                createWalletDepositData));
    }

    @PutMapping("/withdraw/shop")
    public ResponseEntity<?> doWithdrawFromShopWallet(@RequestHeader("Authorization") String authorization, @RequestBody CreateShopWithdrawData createShopWithdrawData) {
        return ResponseEntity
                .status(OK)
                .body(walletsManager.doWithdrawFromShopsWallet(
                        authManager.validateAuthToken(authorization, Roles.MERCHANT_CUSTOMER()),
                        createShopWithdrawData));
    }

    @PutMapping("/changeAmount/{walletId}")
    public ResponseEntity<?> changeWalletAmountByAdmin(@RequestHeader("Authorization") String authorization, @PathVariable String walletId, @RequestParam double newAmount) {
        return ResponseEntity
                .status(OK)
                .body(walletsManager.changeSelectedWalletAmount(
                        authManager.validateAuthToken(authorization, Roles.ADMIN()),
                        walletId, newAmount));
    }

}
