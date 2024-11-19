package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.Candidate;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.repository.PollRepository;
import me.mutashim.votesmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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
    private EmailService emailService;

    public Poll createPoll(String title, String description, LinkedList<Candidate> candidates, String creatorId) {
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

    public Poll getPollWithCandidatesSortedAscending(String pollId) {
        return pollRepository.findById(pollId)
                .map(poll -> {
                    LinkedList<Candidate> sortedCandidates = mergeSort(poll.getCandidates(), true); // Ascending
                    poll.setCandidates(sortedCandidates);
                    return poll;
                })
                .orElseThrow(() -> new IllegalArgumentException("Poll not found."));
    }

    public Poll getPollWithCandidatesSortedDescending(String pollId) {
        return pollRepository.findById(pollId)
                .map(poll -> {
                    LinkedList<Candidate> sortedCandidates = mergeSort(poll.getCandidates(), false); // Descending
                    poll.setCandidates(sortedCandidates);
                    return poll;
                })
                .orElseThrow(() -> new IllegalArgumentException("Poll not found."));
    }

    private LinkedList<Candidate> mergeSort(LinkedList<Candidate> candidates, boolean ascending) {
        if (candidates.size() <= 1) {
            return candidates;
        }

        int mid = candidates.size() / 2;
        LinkedList<Candidate> left = new LinkedList<>(candidates.subList(0, mid));
        LinkedList<Candidate> right = new LinkedList<>(candidates.subList(mid, candidates.size()));

        LinkedList<Candidate> sortedLeft = mergeSort(left, ascending);
        LinkedList<Candidate> sortedRight = mergeSort(right, ascending);

        return merge(sortedLeft, sortedRight, ascending);
    }

    private LinkedList<Candidate> merge(LinkedList<Candidate> left, LinkedList<Candidate> right, boolean ascending) {
        LinkedList<Candidate> merged = new LinkedList<>();

        while (!left.isEmpty() && !right.isEmpty()) {
            boolean condition = ascending
                    ? left.getFirst().getName().compareTo(right.getFirst().getName()) <= 0
                    : left.getFirst().getName().compareTo(right.getFirst().getName()) > 0;

            if (condition) {
                merged.add(left.removeFirst());
            } else {
                merged.add(right.removeFirst());
            }
        }

        merged.addAll(left);
        merged.addAll(right);

        return merged;
    }
}

