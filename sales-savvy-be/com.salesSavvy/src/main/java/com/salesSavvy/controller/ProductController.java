package com.salesSavvy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.salesSavvy.dto.*;
import com.salesSavvy.entity.*;
import com.salesSavvy.exception.BadRequestException;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.service.CartService;
import com.salesSavvy.service.ProductServiceImplementation;
import com.salesSavvy.service.UsersService;

@RestController
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired private ProductServiceImplementation service;
	@Autowired private UsersService uService;
	@Autowired private CartService cService;
	
	// ---------------- Public Endpoints ----------------
	
	@GetMapping("/getAllProducts")
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		List<ProductResponse> products = service.getAllProducts()
			.stream()
			.map(ProductResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}

	@GetMapping("/searchProduct")
	public ResponseEntity<ProductResponse> searchProduct(@RequestParam long id) {
		Product product = service.searchProduct(id);
		return ResponseEntity.ok(new ProductResponse(product));
	}
	
	// Search products by keyword
	@GetMapping("/searchProducts")
	public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
		List<ProductResponse> products = service.searchProductsByName(keyword)
			.stream()
			.map(ProductResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}
	
	// Get products by category
	@GetMapping("/products/category/{category}")
	public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
		List<ProductResponse> products = service.getProductsByCategory(category)
			.stream()
			.map(ProductResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}
	
	// Get all categories
	@GetMapping("/categories")
	public ResponseEntity<List<String>> getAllCategories() {
		return ResponseEntity.ok(service.getAllCategories());
	}
	
	// Filter products by price range
	@GetMapping("/products/price-range")
	public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
			@RequestParam BigDecimal minPrice,
			@RequestParam BigDecimal maxPrice) {
		
		if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(minPrice) < 0) {
			throw new BadRequestException("Invalid price range");
		}
		
		List<ProductResponse> products = service.getProductsByPriceRange(minPrice, maxPrice)
			.stream()
			.map(ProductResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}
	
	// Get products sorted by price
	@GetMapping("/products/sorted")
	public ResponseEntity<List<ProductResponse>> getProductsSorted(
			@RequestParam(defaultValue = "asc") String order) {
		
		boolean ascending = "asc".equalsIgnoreCase(order);
		List<ProductResponse> products = service.getProductsSortedByPrice(ascending)
			.stream()
			.map(ProductResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}
	
	// Advanced filter: category + price range
	@GetMapping("/products/filter")
	public ResponseEntity<List<ProductResponse>> filterProducts(
			@RequestParam String category,
			@RequestParam BigDecimal minPrice,
			@RequestParam BigDecimal maxPrice) {
		
		List<ProductResponse> products = service.getProductsByCategoryAndPrice(category, minPrice, maxPrice)
			.stream()
			.map(ProductResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}

	// ---------------- Admin Only Endpoints ----------------
	
	@PostMapping("/addProduct")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody Product product) {
		service.addProduct(product);
		logger.info("Product added: {}", product.getName());
		
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ProductResponse(product));
	}

	@PutMapping("/updateProduct/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> updateProduct(
			@PathVariable Long id, 
			@Valid @RequestBody Product product) {
		
		product.setId(id);
		service.updateProduct(product);
		
		logger.info("Product updated: {}", id);
		return ResponseEntity.ok(new ProductResponse(service.searchProduct(id)));
	}

	@DeleteMapping("/deleteProduct/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
		service.deleteProduct(id);
		logger.info("Product deleted: {}", id);
		return ResponseEntity.ok("Product deleted successfully");
	}

	// ---------------- Customer Cart Endpoints ----------------
	
	@PostMapping("/addToCart")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
	public ResponseEntity<CartResponse> addToCart(@RequestBody CartItem item) {
		
		if (item.getProductId() == null) {
			throw new BadRequestException("productId is required");
		}
		
		if (item.getQuantity() <= 0) {
			throw new BadRequestException("quantity must be greater than 0");
		}

		Users user = uService.getUser(item.getUsername());
		if (user == null) {
			throw new ResourceNotFoundException("User", "username", item.getUsername());
		}

		Product product = service.searchProduct(item.getProductId());

		Cart cart = user.getCart();
		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			user.setCart(cart);
			cService.addCart(cart);
		}

		List<CartItem> items = cart.getCartItems();
		if (items == null) {
			items = new ArrayList<>();
		}

		boolean found = false;
		for (CartItem ci : items) {
			if (ci.getProduct().getId().equals(product.getId())) {
				ci.setQuantity(ci.getQuantity() + item.getQuantity());
				found = true;
				break;
			}
		}

		if (!found) {
			CartItem newItem = new CartItem();
			newItem.setCart(cart);
			newItem.setProduct(product);
			newItem.setQuantity(item.getQuantity());
			items.add(newItem);
		}

		cart.setCartItems(items);
		cService.addCart(cart);

		logger.info("Product added to cart: {} for user: {}", product.getName(), user.getUsername());
		return ResponseEntity.ok(new CartResponse(cart));
	}

	@GetMapping("/getCart/{username}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
	public ResponseEntity<CartResponse> getCart(@PathVariable String username) {
		
		Users u = uService.getUser(username);
		if (u == null) {
			throw new ResourceNotFoundException("User", "username", username);
		}
		
		if (u.getCart() == null) {
			Cart emptyCart = new Cart();
			emptyCart.setUser(u);
			return ResponseEntity.ok(new CartResponse(emptyCart));
		}
		
		return ResponseEntity.ok(new CartResponse(u.getCart()));
	}
	
	@DeleteMapping("/cart/item/{itemId}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
	public ResponseEntity<String> removeFromCart(
			@PathVariable Long itemId,
			@RequestParam String username) {
		
		Users user = uService.getUser(username);
		if (user == null || user.getCart() == null) {
			throw new ResourceNotFoundException("Cart not found for user: " + username);
		}
		
		Cart cart = user.getCart();
		boolean removed = cart.getCartItems().removeIf(item -> item.getId().equals(itemId));
		
		if (!removed) {
			throw new ResourceNotFoundException("Cart item", "id", itemId);
		}
		
		cService.addCart(cart);
		logger.info("Item removed from cart: {} for user: {}", itemId, username);
		
		return ResponseEntity.ok("Item removed from cart");
	}
	
	@PutMapping("/cart/item/{itemId}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
	public ResponseEntity<CartResponse> updateCartItemQuantity(
			@PathVariable Long itemId,
			@RequestParam String username,
			@RequestParam int quantity) {
		
		if (quantity <= 0) {
			throw new BadRequestException("Quantity must be greater than 0");
		}
		
		Users user = uService.getUser(username);
		if (user == null || user.getCart() == null) {
			throw new ResourceNotFoundException("Cart not found for user: " + username);
		}
		
		Cart cart = user.getCart();
		CartItem item = cart.getCartItems().stream()
			.filter(ci -> ci.getId().equals(itemId))
			.findFirst()
			.orElseThrow(() -> new ResourceNotFoundException("Cart item", "id", itemId));
		
		item.setQuantity(quantity);
		cService.addCart(cart);
		
		logger.info("Cart item quantity updated: {} to {} for user: {}", 
			itemId, quantity, username);
		
		return ResponseEntity.ok(new CartResponse(cart));
	}
}