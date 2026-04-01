package com.bank;

import com.bank.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Bank Management System Spring Boot application.
 * This class bootstraps the entire application.
 */
@SpringBootApplication
public class BankManagementApplication implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(BankManagementApplication.class, args);
        System.out.println("====================================");
        System.out.println(" Bank Management System is running!");
        System.out.println(" API Base URL: http://localhost:8080/api");
        System.out.println(" Admin Login: admin@bank.com / admin123");
        System.out.println("====================================");
    }

    @Override
    public void run(String... args) throws Exception {
        authService.initializeAdminUser();
    }
}
