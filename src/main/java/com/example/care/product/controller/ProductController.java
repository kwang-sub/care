package com.example.care.product.controller;

import com.example.care.product.dto.ProductDTO;
import com.example.care.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public String servicesPage(Model model) {

        List<ProductDTO> productList = productService.productList();
        model.addAttribute("productList", productList);
        return "/product/productList";
    }
}
