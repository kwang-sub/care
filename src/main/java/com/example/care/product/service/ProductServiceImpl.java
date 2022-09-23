package com.example.care.product.service;

import com.example.care.product.domain.Product;
import com.example.care.product.dto.ProductDTO;
import com.example.care.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> productList() {
        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(this::productEntityToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO productEntityToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .code(product.getCode())
                .title(product.getTitle())
                .description(product.getDescription())
                .build();
    }
}
