package com.application.interfaces;

import com.application.entities.Customer;
import com.application.entities.Product;
import com.application.entities.ReceiptRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByDisplayNameContainingAndInventoryGreaterThan(String string, Integer integer);
    List<Product> findByInventoryGreaterThan(Integer integer);
}