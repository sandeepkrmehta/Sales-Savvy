package com.salesSavvy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.salesSavvy.entity.*;
import com.salesSavvy.service.UsersService;

@CrossOrigin("*")
@RestController
public class UsersController {
	@Autowired
	UsersService service;
	
	// ---------------- SignUp ----------------
	@PostMapping("/signUp")
	public String signUp(@RequestBody Users user) {
		String msg = "";
		String username = user.getUsername();
		Users u = service.getUser(username);
		if(u == null) {
			service.signUp(user);
			msg = "User created successfully!";
		}
		else
			msg = "Username already exists!";
		return msg;
	}

	// ---------------- SignIn ----------------
		@PostMapping("/signIn")
		public String signIn(@RequestBody UserLoginData userLoginData) {
			String username = userLoginData.getUsername();
			String password = userLoginData.getPassword();

			Users u = service.getUser(username);

			if (u == null) {
				return "Username does not exist!";
			} else {
				if (u.getPassword().equals(password)) { // Plain text for now
					if ("admin".equalsIgnoreCase(u.getRole())) {
						return "admin";
					} else {
						return "customer";
					}
				} else {
					return "wrong password";
				}
			}
		}

		// ---------------- Get All Users (Admin Only) ----------------
		@GetMapping("/user")
		public List<Users> getAllUsers() {
			// Ideally, check if the requester is admin before returning all users
			return service.getAllUsers();
		}
		
		@PutMapping("/user/{id}") 
			public String updateUser(@PathVariable Long id, @RequestBody Users user) {
				user.setId(id);
				return service.updateUser(user);
			}
		
		// DELETE user by id -by admin
		@DeleteMapping("/deleteUser/{id}")
		public String deleteUser(@PathVariable Long id) {
			return service.deleteUser(id);
		}
}
