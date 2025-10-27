package com.salesSavvy.controller;

import com.salesSavvy.dto.UserResponse;
import com.salesSavvy.entity.Users;
import com.salesSavvy.security.JwtUtil;
import com.salesSavvy.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UsersService usersService;
    private final JwtUtil jwtUtil;

    public UserController(UsersService usersService, JwtUtil jwtUtil) {
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<Users> users = usersService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
            .map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getDob(),
                user.getRole()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        Users user = usersService.getUser(username);
        UserResponse response = new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getGender(),
            user.getDob(),
            user.getRole()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody Users user) {
        user.setId(id);
        String result = usersService.updateUser(user);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String result = usersService.deleteUser(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getCurrentUserProfile(@RequestHeader("Authorization") String token) {
        String username = extractUsernameFromToken(token);
        Users user = usersService.getUser(username);
        UserResponse response = new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getGender(),
            user.getDob(),
            user.getRole()
        );
        return ResponseEntity.ok(response);
    }

    private String extractUsernameFromToken(String token) {
        // Remove "Bearer " prefix
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return jwtUtil.extractUsername(jwt);
        }
        throw new RuntimeException("Invalid token format");
    }
}