package com.salesSavvy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesSavvy.entity.Cart;
import com.salesSavvy.entity.CartItem;
import com.salesSavvy.entity.Product;
import com.salesSavvy.repository.CartItemRepository;
import com.salesSavvy.repository.CartRepository;
import com.salesSavvy.repository.ProductRepository;

@Service
public class ProductServiceImplementation
				implements ProductService {
	@Autowired
	ProductRepository repo;
	
	 @Autowired
	 CartItemRepository cartItemRepo;

	@Override
	public String addProduct(Product product) {
		repo.save(product);
		return "product added successfully!";
	}

	@Override
	public Product searchProduct(long id) {
		return repo.findById(id).get();
	}

	@Override
	public Product searchProduct(String name) {
		return repo.findByName(name);
	}

//	@Override
//	public String updateProduct(Product product) {
//		repo.save(product);
//		return "product updated successfully!";
//	}
	
	@Override
	public String updateProduct(Product product) {
	    if (product.getCategory() == null || product.getCategory().isBlank()) {
	        return "Category cannot be blank!";
	    }
	    repo.save(product);
	    return "Product updated successfully!";
	}


//	@Override
//	public String deleteProduct(long id) {
//		repo.deleteById(id);
//		return "product deleted successfully!";
//	}
	
	@Override
    public String deleteProduct(long id) {
        Product product = repo.findById(id).orElse(null);
        if (product == null) return "Product not found";

        // Check if product exists in any cart
        List<CartItem> items = cartItemRepo.findByProduct(product);
        if (!items.isEmpty()) {
            return "Cannot delete product, it exists in cart!";
        }

        repo.deleteById(id);
        return "Product deleted successfully!";
    }

	@Override
	public Product searchProductByCategory(String category) {
		return repo.findByCategory(category);
	}

	@Override
	public List<Product> getAllProducts() {
		return repo.findAll();
	}

}
