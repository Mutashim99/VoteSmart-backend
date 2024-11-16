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


    @GetMapping("/me")  // You can use "/me" to refer to the logged-in user's own data
    public ResponseEntity<User> getUserById(HttpSession session) {
        String sessionUserId = (String) session.getAttribute("userId");
        if (sessionUserId == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized access if no user is logged in
        }
        Optional<User> user = userService.getUserById(sessionUserId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping("/me/polls")  // Use "/me/polls" to refer to the logged-in user's polls
    public ResponseEntity<List<Poll>> getUserPolls(HttpSession session) {
        String sessionUserId = (String) session.getAttribute("userId");
        if (sessionUserId == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized access if no user is logged in
        }
        List<Poll> polls = userService.getUserPolls(sessionUserId);
        return ResponseEntity.ok(polls);
    }

}
