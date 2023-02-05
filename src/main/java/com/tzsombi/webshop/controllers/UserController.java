package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.auth.AuthenticationResponse;
import com.tzsombi.webshop.constants.Constants;
import com.tzsombi.webshop.models.UserRequestDTO;
import com.tzsombi.webshop.models.UserResponseDTO;
import com.tzsombi.webshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO userDTO = userService.getUserById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public AuthenticationResponse updateUser(@PathVariable Long userId, @RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUserById(userId, userRequestDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}
