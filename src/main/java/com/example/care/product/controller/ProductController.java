package com.example.care.product.controller;

import com.example.care.product.dto.ProductDTO;
import com.example.care.product.service.ProductService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String servicesPage(Model model) {

        List<ProductDTO> productList = productService.productList();
        model.addAttribute("productList", productList);
        return "/product/list";
    }

}
