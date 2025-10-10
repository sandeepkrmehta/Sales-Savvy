package com.salesSavvy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.salesSavvy.entity.Users;
import com.salesSavvy.exception.DuplicateResourceException;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.repository.UsersRepository;

@Service
public class UsersServiceImplementation implements UsersService {
	
	@Autowired
	private UsersRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void signUp(Users user) {
		// Use efficient existence check
		if (repo.existsByUsername(user.getUsername())) {
			throw new DuplicateResourceException("User", "username", user.getUsername());
		}
		
		if (repo.existsByEmail(user.getEmail())) {
			throw new DuplicateResourceException("User", "email", user.getEmail());
		}
		
		// Encrypt password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		repo.save(user);
	}

	@Override
	public Users getUser(String username) {
		return repo.findByUsername(username);
	}
	
	// Get user with cart loaded (use when cart data is needed)
	public Users getUserWithCart(String username) {
		return repo.findWithCartByUsername(username)
			.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}
	
	// Get user with orders loaded (use when order history is needed)
	public Users getUserWithOrders(String username) {
		return repo.findWithOrdersByUsername(username)
			.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}

	@Override
	public boolean validate(String username, String password) {
		Users user = getUser(username);
		if (user == null) {
			return false;
		}
		// Use password encoder for secure comparison
		return passwordEncoder.matches(password, user.getPassword());
	}
	
	@Override
	public String updateUser(Users user) {
		if (user.getId() == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		
		Users existingUser = repo.findById(user.getId())
			.orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId()));

		// Check for duplicate username (if changing)
		if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
			if (repo.existsByUsername(user.getUsername())) {
				throw new DuplicateResourceException("User", "username", user.getUsername());
			}
			existingUser.setUsername(user.getUsername());
		}
		
		// Check for duplicate email (if changing)
		if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
			if (repo.existsByEmail(user.getEmail())) {
				throw new DuplicateResourceException("User", "email", user.getEmail());
			}
			existingUser.setEmail(user.getEmail());
		}
		
		// Update password if provided
		if (user.getPassword() != null && !user.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		// Update role if provided
		if (user.getRole() != null && !user.getRole().isBlank()) {
			existingUser.setRole(user.getRole());
		}
		
		// Update other fields
		if (user.getGender() != null) existingUser.setGender(user.getGender());
		if (user.getDob() != null) existingUser.setDob(user.getDob());

		repo.save(existingUser);
		return "User updated successfully!";
	}

	@Override
	public String deleteUser(Long id) {
		Users user = repo.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

		if (user.getOrders() != null && !user.getOrders().isEmpty()) {
			throw new IllegalStateException("Cannot delete user: User has existing orders!");
		}

		repo.deleteById(id);
		return "User deleted successfully!";
	}

	@Override
	public List<Users> getAllUsers() {
		return repo.findAll();
	}
}