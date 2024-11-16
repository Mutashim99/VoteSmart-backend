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

import java.util.List;
import java.util.Optional;

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

    // Fetch polls that are not approved
    public List<Poll> getPendingPolls() {
        return pollRepository.findByApproved(false);
    }

    // Check Admin Credentials
    public boolean authenticateAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

    // Approve a poll
    public String approvePoll(String pollId) {
        Optional<Poll> pollOptional = pollRepository.findById(pollId);
        if (pollOptional.isPresent()) {
            Poll poll = pollOptional.get();
            poll.setApproved(true); // Set the poll as approved
            pollRepository.save(poll);

            // Send approval email to the poll creator
            Optional<User> userOptional = userRepository.findById(poll.getCreatorId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Build structured email content
                String emailBody = buildPollApprovalEmail(poll, user);

                emailService.sendEmail(user.getEmail(), "Your Poll has been Approved", emailBody);
            }

            return "Poll approved successfully.";
        } else {
            return "Poll not found.";
        }
    }

    // Reject a poll and delete it from the database, also remove the poll ID from the user's pollIds list
    public String rejectPoll(String pollId) {
        Optional<Poll> pollOptional = pollRepository.findById(pollId);
        if (pollOptional.isPresent()) {
            Poll poll = pollOptional.get();

            // Set the poll as rejected
            poll.setApproved(false); // Update the poll status to rejected
            pollRepository.save(poll); // Save the poll after status update

            // Send rejection email to the poll creator
            Optional<User> userOptional = userRepository.findById(poll.getCreatorId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Build structured email content
                String emailBody = buildPollRejectionEmail(poll, user);

                // Send rejection email
                emailService.sendEmail(user.getEmail(), "Your Poll has been Rejected", emailBody);

                // Remove the poll ID from the user's list of pollIds
                List<String> pollIds = user.getPollIds();
                if (pollIds != null && pollIds.contains(pollId)) {
                    pollIds.remove(pollId);  // Remove the rejected poll ID from the user's poll list
                    user.setPollIds(pollIds);
                    userRepository.save(user); // Save the updated user object
                }
            }

            // Delete the rejected poll from the database
            pollRepository.delete(poll);

            return "Poll rejected, email sent, and deleted successfully.";
        } else {
            return "Poll not found.";
        }
    }

    // Create default admin if not exists
    @PostConstruct
    public void createDefaultAdmin() {
        Admin admin = adminRepository.findByUsername("admin");
        if (admin == null) {
            admin = new Admin(); // Create a new admin if not found
            admin.setUsername("admin");  // Set default username
            admin.setPassword("admin");  // Set default password
            adminRepository.save(admin); // Save the default admin
        }
    }

    // Build structured email body for poll approval
    private String buildPollApprovalEmail(Poll poll, User user) {
        return "<h2>Your Poll has been Approved</h2>"
                + "<p><strong>Poll Title:</strong> " + poll.getTitle() + "</p>"
                + "<p><strong>Poll Description:</strong> " + poll.getDescription() + "</p>"
                + "<p><strong>Created by:</strong> " + user.getName() + " (" + user.getEmail() + ")</p>"
                + "<p><strong>Poll URL:</strong> <a href='http://localhost:8080/poll/" + poll.getId() + "'>View Poll</a></p>"
                + "<p>Your poll is now live and ready to receive votes.</p>"
                + "<p>Thank you for using our platform!</p>";
    }

    // Build structured email body for poll rejection
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
