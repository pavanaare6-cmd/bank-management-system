package com.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller - Provides public access to the root endpoint.
 */
@Controller
public class HomeController {

    /**
     * Public welcome page at root URL.
     * This allows browser access to http://localhost:8080/ without authentication.
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/index.html"; // Redirect to the frontend
    }
}