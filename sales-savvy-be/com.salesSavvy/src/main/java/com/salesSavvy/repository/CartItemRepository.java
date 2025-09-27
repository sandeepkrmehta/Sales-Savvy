package com.salesSavvy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.salesSavvy.entity.CartItem;
import com.salesSavvy.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByProduct(Product product);
}
