package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SocialMediaController {

    private final AccountService accountSvc;
    private final MessageService messageSvc;

    public SocialMediaController(AccountService accountSvc, MessageService messageSvc) {
        this.accountSvc = accountSvc;
        this.messageSvc = messageSvc;
    }

    // User Registration 
    
    //POST /register
    //Creates a new Account. Returns 200 + Account JSON on success,
    //409 if username taken, or 400 on bad input.
 
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account input) {
        try {
            Account created = accountSvc.register(input);
            return ResponseEntity.ok(created);
            // map IllegalStateException → 409 Conflict
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Login 
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account input) {
        try {
            Account acc = accountSvc.login(input);
            return ResponseEntity.ok(acc);
        } catch (SecurityException e) {
            return ResponseEntity.status(401).build();
        }
    }

    // Create New Message 
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message m) {
        try {
            Message saved = messageSvc.create(m);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get All Messages
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageSvc.getAll();
    }

    //  Get One Message 
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Integer id) {
        Message m = messageSvc.getOne(id);
        return ResponseEntity.ok(m);  // will be null if not found
    }

    // Delete a Message
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer id) {
        int deleted = messageSvc.delete(id);
        return deleted == 0
            ? ResponseEntity.ok().build()
            : ResponseEntity.ok(deleted);
    }

    // Update Message Text 
    @PatchMapping("/messages/{id}")
    public ResponseEntity<Integer> updateMessage(
            @PathVariable Integer id,
            @RequestBody Message m
    ) {
        try {
            int updated = messageSvc.updateText(id, m.getMessageText());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get All Messages From a User 
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable Integer accountId) {
        return messageSvc.getByUser(accountId);
    }
}
