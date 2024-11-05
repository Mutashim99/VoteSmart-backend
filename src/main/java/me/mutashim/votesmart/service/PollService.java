package me.mutashim.votesmart.service;
import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
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

    // Create a new poll
    public Poll createPoll(String title, String description, List<Candidate> candidates, String creatorId) {
        // Assign unique IDs to each candidate
        candidates.forEach(candidate -> candidate.setId(UUID.randomUUID().toString()));

        // Create the Poll object with updated candidates
        Poll poll = new Poll(title, description, candidates, creatorId);
        Poll savedPoll = pollRepository.save(poll);

        // Link poll with the user who created it
        userRepository.findById(creatorId).ifPresent(user -> {
            user.getPollIds().add(savedPoll.getId());
            userRepository.save(user);
        });

        return savedPoll;
    }

    public List<Poll> getPollsByUserId(String userId) {
        return pollRepository.findByCreatorId(userId); // Fetch polls by creator ID
    }

    // Retrieve all polls
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    // Retrieve a poll by ID
    public Optional<Poll> getPollById(String pollId) {
        return pollRepository.findById(pollId);
    }

    // Add a candidate to an existing poll
    public void addCandidateToPoll(String pollId, Candidate candidate) {
        pollRepository.findById(pollId).ifPresent(poll -> {
            poll.getCandidates().add(candidate);
            pollRepository.save(poll);
        });
    }

    // Retrieve candidates for a specific poll
    public List<Candidate> getCandidatesForPoll(String pollId) {
        return pollRepository.findById(pollId)
                .map(Poll::getCandidates)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found."));
    }

    // Remove a candidate from a poll
    public void removeCandidateFromPoll(String pollId, String candidateId) {
        pollRepository.findById(pollId).ifPresent(poll -> {
            poll.getCandidates().removeIf(candidate -> candidate.getId().equals(candidateId));
            pollRepository.save(poll);
        });
    }
}
