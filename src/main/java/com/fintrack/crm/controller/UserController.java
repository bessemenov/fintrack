package com.fintrack.crm.controller;

import com.fintrack.crm.dto.UserResponseDTO;
import com.fintrack.crm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("Test endpoint called.");
        return ResponseEntity.ok("working.");
    }

    @GetMapping("/details")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("User details requested.");
        return ResponseEntity.ok(userService.getAllUsers());
    }
}









