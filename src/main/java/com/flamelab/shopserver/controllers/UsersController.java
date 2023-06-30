package com.flamelab.shopserver.controllers;

import com.flamelab.shopserver.dtos.create.CreateUserDto;
import com.flamelab.shopserver.dtos.transfer.TransferTemporaryCodeDto;
import com.flamelab.shopserver.dtos.update.RecoverPasswordDto;
import com.flamelab.shopserver.dtos.update.UpdateUserDto;
import com.flamelab.shopserver.dtos.update.UpdateUserPasswordDto;
import com.flamelab.shopserver.enums.Roles;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.managers.AuthManager;
import com.flamelab.shopserver.managers.UsersManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.flamelab.shopserver.enums.Roles.ADMIN;
import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final AuthManager authManager;
    private final UsersManager usersManager;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto createUserDto) {
        if (createUserDto.getRole().equals(ADMIN)) {
            throw new ResourceException(UNAUTHORIZED, "Can't create ADMIN user by this API.");
        }
        return ResponseEntity
                .status(CREATED)
                .body(usersManager.createUser(createUserDto));
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(@RequestHeader("Authorization") String authorization, @RequestBody CreateUserDto createUserDto) {
        if (!createUserDto.getRole().equals(ADMIN)) {
            throw new ResourceException(BAD_REQUEST, "Please create only ADMIN users via this API.");
        }
        return ResponseEntity
                .status(CREATED)
                .body(usersManager.createUserAdmin(
                        authManager.validateAuthToken(authorization, Roles.ADMIN()),
                        createUserDto));
    }

    @GetMapping("/confirmRegistration/{userId}/{tempCode}")
    public ResponseEntity<?> confirmRegistration(@PathVariable String userId, @PathVariable int tempCode) {
        return ResponseEntity
                .status(OK)
                .body(usersManager.confirmRegistration(userId, tempCode));
    }

    @GetMapping("/activateUser/{userId}")
    public ResponseEntity<?> activateUser(@RequestHeader("Authorization") String authorization, @PathVariable("userId") String userId) {
        return ResponseEntity
                .status(OK)
                .body(usersManager.activateUser(
                        authManager.validateAuthToken(authorization, ADMIN()),
                        userId));
    }

    @PostMapping("/passwordRecovery/tempCode")
    public ResponseEntity<?> sendTemporaryCode(@RequestParam String email) {
        usersManager.sendTemporaryCodeToEmail(email);
        return ResponseEntity
                .status(OK)
                .build();
    }

    @PutMapping("/passwordRecovery/tempCode/verify")
    public ResponseEntity<?> verifyTemporaryCode(@RequestBody TransferTemporaryCodeDto temporaryCodeDto) {
        usersManager.verifyTempCode(temporaryCodeDto);
        return ResponseEntity
                .status(OK)
                .build();
    }

    @PutMapping("/passwordRecovery/updatePassword")
    public ResponseEntity<?> recoverPassword(@RequestBody RecoverPasswordDto recoverPasswordDto) {
        usersManager.recoverPassword(recoverPasswordDto);
        return ResponseEntity
                .status(OK)
                .build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String authorization, @PathVariable("userId") String userId) {
        return ResponseEntity
                .status(OK)
                .body(usersManager.getUserById(
                        authManager.validateAuthToken(authorization, Roles.ADMIN_CUSTOMER_MERCHANT()),
                        userId));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity
                .status(OK)
                .body(usersManager.getAllUsers(authManager.validateAuthToken(authorization, Roles.ADMIN())));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestHeader("Authorization") String authorization, @RequestParam String text) {
        return ResponseEntity
                .status(OK)
                .body(usersManager.getAllUsersByTextInParameters(
                        authManager.validateAuthToken(authorization, Roles.ADMIN()),
                        text));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserData(@RequestHeader("Authorization") String authorization, @PathVariable("userId") String userId, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity
                .status(OK)
                .body(usersManager.updateUserData(
                        authManager.validateAuthToken(authorization, Roles.ADMIN_CUSTOMER_MERCHANT()),
                        userId, updateUserDto));
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String authorization, @RequestBody UpdateUserPasswordDto updateUserPasswordDto) {
        usersManager.updateUserPassword(
                authManager.validateAuthToken(authorization, Roles.ADMIN_CUSTOMER_MERCHANT()),
                updateUserPasswordDto);
        return ResponseEntity
                .status(OK)
                .build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authorization, @PathVariable("userId") String userId) {
        usersManager.deleteUser(authManager.validateAuthToken(authorization, Roles.ADMIN_CUSTOMER_MERCHANT()), userId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

}
