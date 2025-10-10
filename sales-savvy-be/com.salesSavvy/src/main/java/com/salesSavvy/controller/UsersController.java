package com.salesSavvy.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.salesSavvy.dto.AuthResponse;
import com.salesSavvy.dto.UserResponse;
import com.salesSavvy.entity.*;
import com.salesSavvy.exception.BadRequestException;
import com.salesSavvy.exception.DuplicateResourceException;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.security.JwtUtil;
import com.salesSavvy.service.UsersService;

@RestController
public class UsersController {
	
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired private UsersService service;
	@Autowired private JwtUtil jwtUtil;
	
	// ---------------- SignUp ----------------
	@PostMapping("/signUp")
	public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody Users user) {
		
		Users existingUser = service.getUser(user.getUsername());
		if (existingUser != null) {
			throw new DuplicateResourceException("User", "username", user.getUsername());
		}
		
		// Default role to CUSTOMER if not specified
		if (user.getRole() == null || user.getRole().isBlank()) {
			user.setRole("CUSTOMER");
		}
		
		// Validate role
		if (!List.of("CUSTOMER", "ADMIN").contains(user.getRole().toUpperCase())) {
			throw new BadRequestException("Invalid role. Must be CUSTOMER or ADMIN");
		}
		
		service.signUp(user);
		logger.info("User registered: {}", user.getUsername());
		
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new AuthResponse(null, user.getUsername(), user.getRole(), 
				"User created successfully!"));
	}

	// ---------------- SignIn ----------------
	@PostMapping("/signIn")
	public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody UserLoginData userLoginData) {
		
		String username = userLoginData.getUsername();
		String password = userLoginData.getPassword();

		Users user = service.getUser(username);
		if (user == null) {
			throw new ResourceNotFoundException("User", "username", username);
		}

		if (!service.validate(username, password)) {
			logger.warn("Failed login attempt for user: {}", username);
			throw new BadRequestException("Wrong password");
		}

		// Generate JWT token
		String token = jwtUtil.generateToken(username, user.getRole());
		logger.info("User logged in: {}", username);
		
		return ResponseEntity.ok(new AuthResponse(token, username, user.getRole(), 
			"Login successful"));
	}

	// ---------------- Get All Users (Admin Only) ----------------
	@GetMapping("/user")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		
		List<UserResponse> users = service.getAllUsers()
			.stream()
			.map(UserResponse::new)
			.collect(Collectors.toList());
		
		logger.info("Admin retrieved all users. Total: {}", users.size());
		return ResponseEntity.ok(users);
	}
	
	// ---------------- Get User by Username ----------------
	@GetMapping("/user/{username}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
	public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
		
		Users user = service.getUser(username);
		if (user == null) {
			throw new ResourceNotFoundException("User", "username", username);
		}
		
		return ResponseEntity.ok(new UserResponse(user));
	}
	
	// ---------------- Update User (Admin Only) ----------------
	@PutMapping("/user/{id}") 
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable Long id, 
			@Valid @RequestBody Users user) {
		
		user.setId(id);
		String result = service.updateUser(user);
		
		if (result.contains("not found")) {
			throw new ResourceNotFoundException("User", "id", id);
		}
		
		if (result.contains("cannot be blank")) {
			throw new BadRequestException(result);
		}
		
		Users updatedUser = service.getAllUsers().stream()
			.filter(u -> u.getId().equals(id))
			.findFirst()
			.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		
		logger.info("User updated: {}", id);
		return ResponseEntity.ok(new UserResponse(updatedUser));
	}
	
	// ---------------- DELETE user by id (Admin Only) ----------------
	@DeleteMapping("/deleteUser/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		
		String result = service.deleteUser(id);
		
		if (result.contains("not found")) {
			throw new ResourceNotFoundException("User", "id", id);
		}
		
		if (result.contains("Cannot delete")) {
			throw new BadRequestException(result);
		}
		
		logger.info("User deleted: {}", id);
		return ResponseEntity.ok("User deleted successfully");
	}
}