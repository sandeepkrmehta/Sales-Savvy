package com.salesSavvy.service;

import com.salesSavvy.entity.Users;
import java.util.List;

public interface UsersService {
    void signUp(Users user);
    Users getUser(String username);
    boolean validateUser(String username, String password);
    List<Users> getAllUsers();
    String deleteUser(Long id);
    String updateUser(Users user);
    Users getUserWithCart(String username);
    Users getUserWithOrders(String username);
}