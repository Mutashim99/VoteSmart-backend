package me.mutashim.votesmart.service;

import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User user = new User(name, email, password, new LinkedList<>());
        return userRepository.save(user);
    }
}
