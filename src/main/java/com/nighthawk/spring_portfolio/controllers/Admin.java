package com.nighthawk.spring_portfolio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Admin {
    @GetMapping("/adminPanel")
    public String adminPanel() {
        return "adminPanel";
    }
}
