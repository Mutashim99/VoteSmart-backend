package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {

            User createdUser = signupService.registerUser(user.getName(), user.getEmail(), user.getPassword());
            return ResponseEntity.ok("User registered successfully: " + createdUser.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
