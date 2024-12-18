package me.mutashim.votesmart.service;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {
    private final Map<String, String> predefinedResponses;

    public ChatService() {
        predefinedResponses = new HashMap<>();
        predefinedResponses.put("how to create a poll", "To create a poll, log in, go to the 'Create Poll' section, fill in the required details, and click 'Create'.");
        predefinedResponses.put("how to vote", "Log in, navigate to the poll you want to vote on, select your preferred candidate, and click 'Vote'.");
        predefinedResponses.put("can i vote twice", "No, each user can vote only once per poll to maintain fairness.");
        predefinedResponses.put("how to see poll results", "You can view results by navigating to the poll and clicking on 'View Results' if they are public.");
        predefinedResponses.put("what is votesmart", "Votesmart is a platform for creating and participating in polls. Sign up to get started!");
        predefinedResponses.put("how to share a poll", "After creating a poll, you can copy the poll's link and share it with others.");
        predefinedResponses.put("how to delete a poll", "To delete a poll, navigate to 'My Polls', select the poll, and click 'Delete'.");
        predefinedResponses.put("what happens if a poll is rejected", "If a poll is rejected, you will receive an email notification with the reason.");
        predefinedResponses.put("how to add candidates to a poll", "When creating a poll, enter candidate details in the provided fields before submitting.");
        predefinedResponses.put("how to see poll history", "Navigate to 'My Profile' to view your poll history, including created and voted polls.");
        predefinedResponses.put("why is my poll pending", "After creating a poll, it will need to be approved by an admin before it goes live. Please wait for approval.");
        predefinedResponses.put("how long does it take to approve a poll", "The approval time may vary. It typically takes a few hours, but please be patient.");
        predefinedResponses.put("what happens after a poll is approved", "Once your poll is approved, you will be notified, and it will be available for voting.");
        predefinedResponses.put("who approves the polls", "Polls are approved by the admin. Admins ensure that the polls meet the platform's guidelines.");
    }

    public String getResponse(String userMessage) {
        // Normalize the user input (remove special characters and make lowercase)
        String normalizedMessage = userMessage.toLowerCase().trim().replaceAll("[^a-zA-Z0-9 ]", "");
        System.out.println("User Input: " + normalizedMessage);  // Log the normalized input

        // First, check for an exact match with predefined responses
        if (predefinedResponses.containsKey(normalizedMessage)) {
            System.out.println("Exact Match Found: " + normalizedMessage);  // Log exact match
            return predefinedResponses.get(normalizedMessage);  // Return the corresponding response
        }

        // If no exact match, check for partial matches by splitting user input into words
        for (String key : predefinedResponses.keySet()) {
            String normalizedKey = key.toLowerCase().trim().replaceAll("[^a-zA-Z0-9 ]", "");
            System.out.println("Checking Key: " + normalizedKey);  // Log the normalized key

            // Split user input and check each word against the key
            String[] userWords = normalizedMessage.split(" ");
            for (String word : userWords) {
                if (normalizedKey.contains(word)) {
                    System.out.println("Matched by word: " + word);  // Log the matched word
                    return predefinedResponses.get(key);  // Return the corresponding response
                }
            }
        }

        // If no match is found, return a default response
        return "I'm here to help! Please try rephrasing your question or ask something else.";
    }


}