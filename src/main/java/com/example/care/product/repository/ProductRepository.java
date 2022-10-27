package com.example.care.product.repository;

import com.example.care.product.domain.Product;
import com.example.care.product.domain.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByCode(ProductCode code);
}
