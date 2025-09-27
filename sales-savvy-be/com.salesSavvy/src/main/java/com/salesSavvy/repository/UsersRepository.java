package com.salesSavvy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesSavvy.entity.Users;

public interface UsersRepository
		extends JpaRepository<Users, Long> {
	
	 // Fetch a single user by username
    Users findByUsername(String username);

    // Optional: fetch all users by role (e.g., CUSTOMER or ADMIN)
    List<Users> findByRole(String role);
}
