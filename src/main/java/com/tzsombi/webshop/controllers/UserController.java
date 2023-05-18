package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.auth.AuthenticationResponse;
import com.tzsombi.webshop.models.UserRequestDTO;
import com.tzsombi.webshop.models.UserResponseDTO;
import com.tzsombi.webshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDTO me() {
        return userService.getMe();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO userDTO = userService.getUserById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<AuthenticationResponse> updateUser(@PathVariable Long userId, @RequestBody UserRequestDTO userRequestDTO) {
        AuthenticationResponse response = userService.updateUserById(userId, userRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}
