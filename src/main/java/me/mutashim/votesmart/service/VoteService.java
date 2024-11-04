package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.Vote;
import me.mutashim.votesmart.repository.PollRepository;
import me.mutashim.votesmart.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    // Allow a user to vote on a candidate in a poll
    public String vote(String pollId, String candidateId, String voterId) {
        // Check if the user has already voted in this poll
        if (voteRepository.existsByPollIdAndUserId(pollId, voterId)) {
            return "User has already voted in this poll.";
        }

        // Create and save the vote
        Vote vote = new Vote();
        vote.setPollId(pollId);
        vote.setCandidateId(candidateId);
        vote.setUserId(voterId); // Use userId
        voteRepository.save(vote);

        // Update the candidate's vote count in the poll
        pollRepository.findById(pollId).ifPresent(poll -> {
            poll.getCandidates().stream()
                    .filter(candidate -> candidate.getId().equals(candidateId))
                    .findFirst()
                    .ifPresent(candidate -> candidate.setVoteCount(candidate.getVoteCount() + 1));
            pollRepository.save(poll); // Save updated poll
        });

        return "Vote recorded successfully.";
    }

    // Get voting results for a poll
    public List<Candidate> getVotingResults(String pollId) {
        return pollRepository.findById(pollId)
                .map(Poll::getCandidates)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found."));
    }

    // Check if a user has already voted
    public boolean hasVoted(String pollId, String voterId) {
        return voteRepository.existsByPollIdAndUserId(pollId, voterId);
    }
}
