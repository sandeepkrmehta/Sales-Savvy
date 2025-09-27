package com.salesSavvy.service;

import java.util.List;

import com.salesSavvy.entity.Users;

public interface UsersService {
	void signUp(Users user);
	Users getUser(String username);
	boolean validate(String password, String password2);
	List<Users> getAllUsers();
	String deleteUser(Long id);
	String updateUser(Users user);
}
