package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.repository.PollRepository;
import me.mutashim.votesmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // Add email service dependency

    public Poll createPoll(String title, String description, List<Candidate> candidates, String creatorId) {
        candidates.forEach(candidate -> candidate.setId(UUID.randomUUID().toString()));

        Poll poll = new Poll(title, description, candidates, creatorId);
        Poll savedPoll = pollRepository.save(poll);

        userRepository.findById(creatorId).ifPresent(user -> {
            user.getPollIds().add(savedPoll.getId());
            userRepository.save(user);
        });

        return savedPoll;
    }

    public List<Poll> getPollsByUserId(String userId) {
        return pollRepository.findByCreatorId(userId); // Fetch polls by creator ID
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Optional<Poll> getPollById(String pollId) {
        return pollRepository.findById(pollId);
    }

    public void addCandidateToPoll(String pollId, Candidate candidate) {
        pollRepository.findById(pollId).ifPresent(poll -> {
            poll.getCandidates().add(candidate);
            pollRepository.save(poll);
        });
    }

    public List<Candidate> getCandidatesForPoll(String pollId) {
        return pollRepository.findById(pollId)
                .map(Poll::getCandidates)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found."));
    }

    public void removeCandidateFromPoll(String pollId, String candidateId) {
        pollRepository.findById(pollId).ifPresent(poll -> {
            poll.getCandidates().removeIf(candidate -> candidate.getId().equals(candidateId));
            pollRepository.save(poll);
        });
    }


}
