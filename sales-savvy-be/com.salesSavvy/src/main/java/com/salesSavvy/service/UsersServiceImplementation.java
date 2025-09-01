package com.salesSavvy.service;

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
		return (password.equals(dbPassword));
	}
}
