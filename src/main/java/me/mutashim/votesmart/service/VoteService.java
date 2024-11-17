package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.ResponseMessage;
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


    public ResponseMessage vote(String pollId, String candidateId, String voterId) {
        if (voteRepository.existsByPollIdAndUserId(pollId, voterId)) {
            return new ResponseMessage("User has already voted in this poll.", false);
        }

        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isPresent()) {
            Poll poll = optionalPoll.get();

            if (!poll.isApproved()) {
                return new ResponseMessage("This poll is not approved and cannot accept votes.", false);
            }

            Candidate candidate = poll.getCandidates().stream()
                    .filter(c -> c.getId().equals(candidateId))
                    .findFirst()
                    .orElse(null);

            if (candidate == null) {
                return new ResponseMessage("Candidate not found in the poll.", false);
            }

            Vote vote = new Vote(pollId, candidateId, voterId);
            voteRepository.save(vote);

            candidate.setVoteCount(candidate.getVoteCount() + 1);
            pollRepository.save(poll);

            return new ResponseMessage("Vote recorded successfully.", true);
        } else {
            return new ResponseMessage("Poll not found.", false);
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
