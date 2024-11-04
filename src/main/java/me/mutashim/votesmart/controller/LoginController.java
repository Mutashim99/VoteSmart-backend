package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<User> userOptional = loginService.loginUser(email, password);
        if (userOptional.isPresent()) {
            // Store user ID in session
            session.setAttribute("userId", userOptional.get().getId());
            return ResponseEntity.ok("Login successful. User ID stored in session.");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

    @GetMapping("/checkSession")
    public ResponseEntity<String> checkSession(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            return ResponseEntity.ok("User ID in session: " + userId);
        } else {
            return ResponseEntity.status(401).body("No user ID in session.");
        }
    }

}
