package com.salesSavvy.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salesSavvy.entity.Cart;
import com.salesSavvy.entity.CartItem;
import com.salesSavvy.entity.Product;
import com.salesSavvy.entity.Users;
import com.salesSavvy.service.CartService;
import com.salesSavvy.service.ProductService;
import com.salesSavvy.service.UsersService;

//@CrossOrigin("*")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ProductController {
	@Autowired
	ProductService service;

	@Autowired
	UsersService uService;

	@Autowired
	CartService cService;
	
	@GetMapping("/getAllProducts")
	public List<Product> getAllProducts() {
		return service.getAllProducts();
	}

	@PostMapping("/addProduct")
	public String addProduct(@RequestBody Product product) {
		return service.addProduct(product);
	}

	@GetMapping("/searchProduct")
	public Product searchProduct(@RequestParam long id) {
		return service.searchProduct(id);
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@RequestBody Product product) {
		return service.updateProduct(product);
	}
	
	@PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);  // ensure id is set
        return service.updateProduct(product);
    }
	
//	@GetMapping("/deleteProduct")
//	public String deleteProduct(@RequestParam long id) {
//		return service.deleteProduct(id);
//	}

	// DELETE product by id
	@DeleteMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable Long id) {
		return service.deleteProduct(id);
	}


	@PostMapping("/addToCart")
	public String addToCart(@RequestBody CartItem item) {
	    if (item.getProductId() == null) return "productId is required"; // <-- safeguard

	    Users user = uService.getUser(item.getUsername());
	    if (user == null) return "user not found";

	    Product product = service.searchProduct(item.getProductId());
	    if (product == null) return "product not found";

	    Cart cart = user.getCart();
	    if (cart == null) {
	        cart = new Cart();
	        cart.setUser(user);
	        user.setCart(cart);
	        cService.addCart(cart);
	    }

	    List<CartItem> items = cart.getCartItems();
	    if (items == null) items = new ArrayList<>();

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

	    return "cart added";
	}


	@GetMapping("/getCart/{username}")
	public List<CartItem> getCart(@PathVariable String username) {
		Users u = uService.getUser(username);
		if (u == null || u.getCart() == null)
			return new ArrayList<>();
		return u.getCart().getCartItems();
	}
}
