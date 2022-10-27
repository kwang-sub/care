package com.example.care.product.controller;

import com.example.care.product.dto.ProductDTO;
import com.example.care.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String productList(Model model) {

        List<ProductDTO> productList = productService.productList();
        model.addAttribute("productList", productList);
        return "product/list";
    }

}
