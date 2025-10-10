package com.salesSavvy.service;

import java.util.List;

import com.salesSavvy.entity.*;

public interface CartService {
	void addCart(Cart cart);

	void clearCart(String username);

	List<CartItem> getItems(String username);

	List<CartItem> cloneItems(String username);
}
