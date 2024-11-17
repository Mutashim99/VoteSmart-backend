package me.mutashim.votesmart.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            response.put("authenticated", true);
            response.put("userId", userId);
        } else {
            response.put("authenticated", false);
        }
        return ResponseEntity.ok(response);
    }
}
