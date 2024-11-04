package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.repository.PollRepository;
import me.mutashim.votesmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    // Retrieve user by ID
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    // Retrieve polls created by the user
    public List<Poll> getUserPolls(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getPollIds)
                .orElseThrow(() -> new IllegalArgumentException("User not found."))
                .stream()
                .map(pollId -> pollRepository.findById(pollId).orElse(null))
                .filter(poll -> poll != null)
                .collect(Collectors.toList());
    }
}
