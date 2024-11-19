package me.mutashim.votesmart.service;

import jakarta.annotation.PostConstruct;
import me.mutashim.votesmart.model.Admin;
import me.mutashim.votesmart.model.Poll;
import me.mutashim.votesmart.model.User;
import me.mutashim.votesmart.repository.PollRepository;
import me.mutashim.votesmart.repository.UserRepository;
import me.mutashim.votesmart.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class AdminService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmailService emailService;


    public List<Poll> getPendingPolls() {
        return pollRepository.findByApproved(false);
    }


    public boolean authenticateAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }


    public String approvePoll(String pollId) {
        Optional<Poll> pollOptional = pollRepository.findById(pollId);
        if (pollOptional.isPresent()) {
            Poll poll = pollOptional.get();
            poll.setApproved(true);
            pollRepository.save(poll);


            Optional<User> userOptional = userRepository.findById(poll.getCreatorId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();


                String emailBody = buildPollApprovalEmail(poll, user);

                emailService.sendEmail(user.getEmail(), "Your Poll has been Approved", emailBody);
            }

            return "Poll approved successfully.";
        } else {
            return "Poll not found.";
        }
    }


    public String rejectPoll(String pollId) {
        Optional<Poll> pollOptional = pollRepository.findById(pollId);
        if (pollOptional.isPresent()) {
            Poll poll = pollOptional.get();


            poll.setApproved(false);
            pollRepository.save(poll);


            Optional<User> userOptional = userRepository.findById(poll.getCreatorId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();


                String emailBody = buildPollRejectionEmail(poll, user);


                emailService.sendEmail(user.getEmail(), "Your Poll has been Rejected", emailBody);


                Stack<String> pollIds = user.getPollIds();
                if (pollIds != null && pollIds.contains(pollId)) {
                    pollIds.remove(pollId);
                    user.setPollIds(pollIds);
                    userRepository.save(user);
                }
            }


            pollRepository.delete(poll);

            return "Poll rejected, email sent, and deleted successfully.";
        } else {
            return "Poll not found.";
        }
    }


    @PostConstruct
    public void createDefaultAdmin() {
        Admin admin = adminRepository.findByUsername("admin");
        if (admin == null) {
            admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("admin");
            adminRepository.save(admin);
        }
    }


    private String buildPollApprovalEmail(Poll poll, User user) {
        return "<h2>Your Poll has been Approved</h2>"
                + "<p><strong>Poll Title:</strong> " + poll.getTitle() + "</p>"
                + "<p><strong>Poll Description:</strong> " + poll.getDescription() + "</p>"
                + "<p><strong>Created by:</strong> " + user.getName() + " (" + user.getEmail() + ")</p>"
                + "<p><strong>Poll URL:</strong> <a href='http://localhost:8080/poll/" + poll.getId() + "'>View Poll</a></p>"
                + "<p>Your poll is now live and ready to receive votes.</p>"
                + "<p>Thank you for using VoteSmart!</p>";
    }


    private String buildPollRejectionEmail(Poll poll, User user) {
        return "<h2>Your Poll has been Rejected</h2>"
                + "<p><strong>Poll Title:</strong> " + poll.getTitle() + "</p>"
                + "<p><strong>Poll Description:</strong> " + poll.getDescription() + "</p>"
                + "<p><strong>Created by:</strong> " + user.getName() + " (" + user.getEmail() + ")</p>"
                + "<p>Unfortunately, your poll did not meet our community guidelines and has been rejected.</p>"
                + "<p>For more information, please contact support at 2023f-bse-099@ssuet.edu.pk .</p>"
                + "<p>Thank you for understanding!</p>";
    }
}
