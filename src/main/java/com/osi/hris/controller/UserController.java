package com.osi.hris.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.osi.hris.dto.UserRequestDTO;
import com.osi.hris.dto.UserResponseDTO;
import com.osi.hris.entity.User;
import com.osi.hris.service.UserService;

@RestController
@RequestMapping("/User")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create User
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        logger.info("Create User API called with email={}", dto.getEmail());
        UserResponseDTO savedUser = userService.createUser(dto);
        return ResponseEntity.ok(savedUser);
    }

    // Get All Users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Get All Users API called");
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userDTOs = users.stream()
                                              .map(userService::toDTO)
                                              .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // Get User By ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        logger.info("Get User By ID API called with id={}", id);
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.toDTO(user));
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Delete User API called with id={}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
