package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping("/create")
    public ResponseEntity<?> createPoll(
            @RequestParam String title,
            @RequestParam String description,
            @RequestBody LinkedList<Candidate> candidates,
            HttpSession session) {

        String creatorId = (String) session.getAttribute("userId");
        String userId = (String) session.getAttribute("userId");
        System.out.println("User ID in session from PollController: " + userId);
        if (creatorId == null) {
            return ResponseEntity.status(401).body("User not authenticated.");
        }

        Poll poll = pollService.createPoll(title, description, candidates, creatorId);
        return ResponseEntity.ok(poll);
    }

    @GetMapping("/user/polls")
    public ResponseEntity<List<Poll>> getPollsByUser(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(Collections.emptyList());
        }

        List<Poll> userPolls = pollService.getPollsByUserId(userId);
        return ResponseEntity.ok(userPolls);
    }

    @GetMapping
    public ResponseEntity<List<Poll>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<Poll> getPollById(@PathVariable String pollId) {
        Optional<Poll> poll = pollService.getPollById(pollId);
        return poll.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{pollId}/candidates")
    public ResponseEntity<String> addCandidateToPoll(@PathVariable String pollId, @RequestBody Candidate candidate) {
        pollService.addCandidateToPoll(pollId, candidate);
        return ResponseEntity.ok("Candidate added successfully.");
    }

    @DeleteMapping("/{pollId}/candidates/{candidateId}")
    public ResponseEntity<String> removeCandidateFromPoll(@PathVariable String pollId, @PathVariable String candidateId) {
        pollService.removeCandidateFromPoll(pollId, candidateId);
        return ResponseEntity.ok("Candidate removed successfully.");
    }

    @GetMapping("/{pollId}/sorted/asc")
    public ResponseEntity<Poll> getPollWithCandidatesSortedAscending(@PathVariable String pollId) {
        return ResponseEntity.ok(pollService.getPollWithCandidatesSortedAscending(pollId));
    }

    @GetMapping("/{pollId}/sorted/desc")
    public ResponseEntity<Poll> getPollWithCandidatesSortedDescending(@PathVariable String pollId) {
        return ResponseEntity.ok(pollService.getPollWithCandidatesSortedDescending(pollId));
    }


}
