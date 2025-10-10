package com.salesSavvy.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesSavvy.entity.CartItem;
import com.salesSavvy.entity.Product;
import com.salesSavvy.exception.DuplicateResourceException;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.repository.CartItemRepository;
import com.salesSavvy.repository.ProductRepository;

@Service
public class ProductServiceImplementation implements ProductService {
	
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private CartItemRepository cartItemRepo;

	@Override
	public String addProduct(Product product) {
		// Check for duplicate name using efficient query
		if (repo.existsByName(product.getName())) {
			throw new DuplicateResourceException("Product", "name", product.getName());
		}
		
		repo.save(product);
		return "Product added successfully!";
	}

	@Override
	public Product searchProduct(long id) {
		return repo.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
	}

	@Override
	public Product searchProduct(String name) {
		return repo.findByName(name)
			.orElse(null);
	}

	@Override
	public String updateProduct(Product product) {
		if (product.getId() == null) {
			throw new IllegalArgumentException("Product ID cannot be null");
		}
		
		// Verify product exists
		Product existing = repo.findById(product.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Product", "id", product.getId()));
		
		// Check if name is being changed to an existing name
		if (product.getName() != null && !product.getName().equals(existing.getName())) {
			if (repo.existsByName(product.getName())) {
				throw new DuplicateResourceException("Product", "name", product.getName());
			}
		}
		
		// Validate category
		if (product.getCategory() != null && product.getCategory().isBlank()) {
			throw new IllegalArgumentException("Category cannot be blank!");
		}
		
		repo.save(product);
		return "Product updated successfully!";
	}

	@Override
	public String deleteProduct(long id) {
		Product product = repo.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		// Check if product exists in any cart
		List<CartItem> items = cartItemRepo.findByProduct(product);
		if (!items.isEmpty()) {
			throw new IllegalStateException("Cannot delete product, it exists in " + items.size() + " cart(s)!");
		}

		repo.deleteById(id);
		return "Product deleted successfully!";
	}

	@Override
	public Product searchProductByCategory(String category) {
		// Note: This returns only ONE product - might want to change to List
		List<Product> products = repo.findByCategory(category);
		return products.isEmpty() ? null : products.get(0);
	}

	@Override
	public List<Product> getAllProducts() {
		return repo.findAll();
	}
	
	// Additional methods using optimized queries
	
	public List<Product> getProductsByCategory(String category) {
		return repo.findByCategory(category);
	}
	
	public List<Product> getProductsByCategorySortedByPrice(String category) {
		return repo.findByCategoryOrderByPriceAsc(category);
	}
	
	public List<Product> searchProductsByName(String keyword) {
		return repo.searchByName(keyword);
	}
	
	public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
		return repo.findByPriceRange(minPrice, maxPrice);
	}
	
	public List<Product> getProductsByCategoryAndPrice(String category, 
	                                                    BigDecimal minPrice, 
	                                                    BigDecimal maxPrice) {
		return repo.findByCategoryAndPriceRange(category, minPrice, maxPrice);
	}
	
	public List<String> getAllCategories() {
		return repo.findAllCategories();
	}
	
	public List<Product> getProductsSortedByPrice(boolean ascending) {
		return ascending ? repo.findAllByOrderByPriceAsc() : repo.findAllByOrderByPriceDesc();
	}
}