package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.repository.PollRepository;
import me.mutashim.votesmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;


    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }


    public List<Poll> getUserPolls(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getPollIds)
                .orElseThrow(() -> new IllegalArgumentException("User not found."))
                .stream()
                .map(pollId -> pollRepository.findById(pollId).orElse(null))
                .filter(poll -> poll != null)
                .collect(Collectors.toList());
    }


    public String removePollIdFromUser(String userId, String pollId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Stack<String> pollIds = user.getPollIds();
            if (pollIds.contains(pollId)) {
                pollIds.remove(pollId);
                user.setPollIds(pollIds);
                userRepository.save(user);
                return "Poll ID removed from user successfully.";
            } else {
                return "Poll ID not found in the user's list.";
            }
        } else {
            return "User not found.";
        }
    }
}
