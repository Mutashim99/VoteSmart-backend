package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.ResponseMessage;
import me.mutashim.votesmart.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<ResponseMessage> vote(@PathVariable String pollId, @RequestParam String candidateId, HttpSession session) {
        String voterId = (String) session.getAttribute("userId");
        System.out.println("Voter ID from session: " + voterId); // Debugging log

        if (voterId == null) {
            return ResponseEntity.status(401).body(new ResponseMessage("User not authenticated.", false));
        }

        ResponseMessage result = voteService.vote(pollId, candidateId, voterId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{pollId}/results")
    public ResponseEntity<List<Candidate>> getVotingResults(@PathVariable String pollId) {
        List<Candidate> results = voteService.getVotingResults(pollId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{pollId}/hasVoted")
    public ResponseEntity<Boolean> hasVoted(@PathVariable String pollId, HttpSession session) {
        String voterId = (String) session.getAttribute("userId");
        if (voterId == null) {
            return ResponseEntity.status(401).body(false);
        }
        boolean hasVoted = voteService.hasVoted(pollId, voterId);
        return ResponseEntity.ok(hasVoted);
    }
}
