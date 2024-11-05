package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
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
            @RequestBody List<Candidate> candidates,
            HttpSession session) {
        // Attempt to retrieve the user ID from the session
        String creatorId = (String) session.getAttribute("userId");

        if (creatorId == null) {
            return ResponseEntity.status(401).body("User not authenticated.");  // Returning a ResponseEntity<String> here
        }
        // If authenticated, proceed with poll creation
        Poll poll = pollService.createPoll(title, description, candidates, creatorId);
        return ResponseEntity.ok(poll);
    }



    @GetMapping
    public ResponseEntity<List<Poll>> getAllPolls(HttpSession session) {
        String currentUserId = (String) session.getAttribute("userId"); // Retrieve the user ID from the session
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if user ID is not found
        }
        return ResponseEntity.ok(pollService.getPollsByCurrentUser(currentUserId));
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
}
