package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



 // Handles all business logic and validation for user registration and login.
 

@Service
public class AccountService {
    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }


  // Registers a new user if username is non-blank, password ≥ 4 chars,
 // and username isn’t already taken.

    @Transactional
    public Account register(Account input) {
        if (input.getUsername() == null || input.getUsername().isBlank())
            throw new IllegalArgumentException("Username cannot be blank");
        if (input.getPassword() == null || input.getPassword().length() < 4)
            throw new IllegalArgumentException("Password must be at least 4 characters");
        if (repo.findByUsername(input.getUsername()).isPresent())
        // 409 CONFLICT if that username already exists
            throw new IllegalStateException("Username already taken");
        return repo.save(new Account(input.getUsername(), input.getPassword()));
    }

    
    public Account login(Account input) {
        return repo.findByUsername(input.getUsername())
            .filter(acc -> acc.getPassword().equals(input.getPassword()))
            .orElseThrow(() -> new SecurityException("Invalid credentials"));
    }
}
