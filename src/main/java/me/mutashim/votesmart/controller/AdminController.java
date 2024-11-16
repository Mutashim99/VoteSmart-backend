package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Authenticate Admin
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (adminService.authenticateAdmin(username, password)) {
            return ResponseEntity.ok("Admin authenticated successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // Approve a poll
    @PostMapping("/approve/{pollId}")
    public ResponseEntity<String> approvePoll(@PathVariable String pollId) {
        String result = adminService.approvePoll(pollId);
        return ResponseEntity.ok(result);
    }

    // Reject a poll
    @PostMapping("/reject/{pollId}")
    public ResponseEntity<String> rejectPoll(@PathVariable String pollId) {
        String result = adminService.rejectPoll(pollId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/pending-polls")
    public List<Poll> getPendingPolls() {
        return adminService.getPendingPolls();
    }

}
