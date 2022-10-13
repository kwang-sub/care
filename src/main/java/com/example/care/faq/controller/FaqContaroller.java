package com.example.care.faq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/faq")
public class FaqContaroller {
    @GetMapping
    public String faqForm() {

        return "/faq/form";
    }
}
