package me.mutashim.votesmart.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @DeleteMapping
    public ResponseEntity<String> logout(HttpSession session) {
        System.out.println("Logging out, invalidating session: " + session.getId());
        session.invalidate();
        return ResponseEntity.ok("User logged out successfully.");
    }

}
