package com.flamelab.shopserver.controllers;

import com.flamelab.shopserver.enums.Roles;
import com.flamelab.shopserver.managers.AuthManager;
import com.flamelab.shopserver.managers.PurchaseHistoryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/purchaseHistory")
@RequiredArgsConstructor
public class PurchaseHistoryController {

    private final AuthManager authManager;
    private final PurchaseHistoryManager purchaseHistoryManager;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPurchaseHistoryForAuthorizedUser(@RequestHeader("Authorization") String authorization, @PathVariable String userId) {
        return ResponseEntity
                .status(OK)
                .body(purchaseHistoryManager.getPurchaseHistoryForUser(
                        authManager.validateAuthToken(authorization, Roles.ADMIN_CUSTOMER()),
                        userId));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<?> getPurchaseHistoryForShop(@RequestHeader("Authorization") String authorization, @PathVariable String shopId) {
        return ResponseEntity
                .status(OK)
                .body(purchaseHistoryManager.getPurchaseHistoryForShop(
                        authManager.validateAuthToken(authorization, Roles.ADMIN_MERCHANT()),
                        shopId));
    }

    @GetMapping("/shop/{shopId}/product/{productId}")
    public ResponseEntity<?> getPurchaseHistoryForShopByProduct(@RequestHeader("Authorization") String authorization, @PathVariable String shopId, @PathVariable String productId) {
        return ResponseEntity
                .status(OK)
                .body(purchaseHistoryManager.getPurchaseHistoryForShopByProductId(
                        authManager.validateAuthToken(authorization, Roles.ADMIN_MERCHANT()),
                        shopId,
                        productId));
    }

}
