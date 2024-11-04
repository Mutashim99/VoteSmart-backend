package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get user details by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId, HttpSession session) {
        String sessionUserId = (String) session.getAttribute("userId");
        if (sessionUserId == null || !sessionUserId.equals(userId)) {
            return ResponseEntity.status(401).body(null); // Unauthorized access
        }
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get the polls created by a user
    @GetMapping("/{userId}/polls")
    public ResponseEntity<List<Poll>> getUserPolls(@PathVariable String userId, HttpSession session) {
        String sessionUserId = (String) session.getAttribute("userId");
        if (sessionUserId == null || !sessionUserId.equals(userId)) {
            return ResponseEntity.status(401).body(null); // Unauthorized access
        }
        List<Poll> polls = userService.getUserPolls(userId);
        return ResponseEntity.ok(polls);
    }
}
