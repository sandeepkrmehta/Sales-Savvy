package com.salesSavvy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesSavvy.entity.Users;
import com.salesSavvy.repository.UsersRepository;

@Service
public class UsersServiceImplementation
					implements UsersService {
	@Autowired
	UsersRepository repo;

	@Override
	public void signUp(Users user) {
		repo.save(user);
	}
	@Override
	public Users getUser(String username) {
		return repo.findByUsername(username);
	}

	@Override
	public boolean validate(String username, String password) {
		Users user = getUser(username);
		String dbPassword = user.getPassword();
		return (password != null && password.equals(dbPassword));
	}
	
	 // ---------------- Admin operations ----------------
	@Override
	public String updateUser(Users user) {
	    if (user.getId() == null) {
	        return "User ID cannot be blank";
	    }
	    Users existingUser = repo.findById(user.getId()).orElse(null);
	    if (existingUser == null) {
	        return "User not found";
	    }

	    // Update only fields that are sent
	    if (user.getUsername() != null && !user.getUsername().isBlank()) {
	        existingUser.setUsername(user.getUsername());
	    }
	    if (user.getEmail() != null && !user.getEmail().isBlank()) {
	        existingUser.setEmail(user.getEmail());
	    }
	    if (user.getPassword() != null && !user.getPassword().isBlank()) {
	        existingUser.setPassword(user.getPassword());
	    }
	    if (user.getRole() != null && !user.getRole().isBlank()) {
	        existingUser.setRole(user.getRole());
	    }

	    repo.save(existingUser); // now validation passes
	    return "User updated successfully!";
	}

	
	@Override
	public String deleteUser(Long id) {
	    Users user = repo.findById(id).orElse(null);
	    if (user == null) return "User not found";

	    if (user.getOrders() != null && !user.getOrders().isEmpty()) {
	        return "Cannot delete user: User has existing orders!";
	    }

	    repo.deleteById(id);
	    return "User deleted successfully!";
	}


	
	@Override
	public List<Users> getAllUsers() {
	    return repo.findAll();
	}
}
