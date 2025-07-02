package sales.savvy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sales.savvy.entity.Product;
import sales.savvy.service.ProductService;

@CrossOrigin("*")
@RestController
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/addProduct")
    public String addProduct(@RequestBody Product prod) {
        service.addProduct(prod);
        return "Product added successfully!";
    }
    
    @PostMapping("/updateProduct")
    public String updateProduct(@RequestBody Product prod) {
        service.updateProduct(prod);
        return "Product updated successfully!";
    }
    
    @PostMapping("/deleteProduct")
    public void deleteProduct(@RequestParam Long id) {
        service.deleteProduct(id);
    }
    
    @GetMapping("/getAllProducts")
    public List<Product> getAll() {
    	return service.getAllProducts();
    }
}
