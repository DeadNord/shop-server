package com.flamelab.shopserver.controllers;

import com.flamelab.shopserver.dtos.create.CreateAuthTokenDto;
import com.flamelab.shopserver.dtos.transfer.TransferAuthTokenDto;
import com.flamelab.shopserver.enums.Roles;
import com.flamelab.shopserver.managers.AuthManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthManager authManager;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody CreateAuthTokenDto createUserAuthToken) {
        return ResponseEntity
                .status(OK)
                .body(authManager.login(createUserAuthToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader String authorization) {
        authManager.logout(authManager.validateAuthToken(authorization, List.of(Roles.values())));
        return ResponseEntity
                .status(OK)
                .build();
    }

}
