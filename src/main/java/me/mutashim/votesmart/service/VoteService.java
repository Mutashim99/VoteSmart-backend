package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.Vote;
import me.mutashim.votesmart.repository.PollRepository;
import me.mutashim.votesmart.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;


    public String vote(String pollId, String candidateId, String voterId) {

        if (voteRepository.existsByPollIdAndUserId(pollId, voterId)) {
            return "User has already voted in this poll.";
        }


        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isPresent()) {
            Poll poll = optionalPoll.get();
            Candidate candidate = poll.getCandidates().stream()
                    .filter(c -> c.getId().equals(candidateId))
                    .findFirst()
                    .orElse(null);

            if (candidate == null) {
                return "Candidate not found in the poll.";
            }


            Vote vote = new Vote(pollId, candidateId, voterId);
            voteRepository.save(vote);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            pollRepository.save(poll);

            return "Vote recorded successfully.";
        } else {
            return "Poll not found.";
        }
    }



    public List<Candidate> getVotingResults(String pollId) {
        return pollRepository.findById(pollId)
                .map(Poll::getCandidates)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found."));
    }


    public boolean hasVoted(String pollId, String voterId) {
        return voteRepository.existsByPollIdAndUserId(pollId, voterId);
    }
}
