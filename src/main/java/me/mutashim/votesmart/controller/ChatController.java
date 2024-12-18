package me.mutashim.votesmart.controller;

import me.mutashim.votesmart.model.ChatMessage;
import me.mutashim.votesmart.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage userMessage) {
        String userInput = userMessage.getContent();
        String botResponse = chatService.getResponse(userInput);

        return new ChatMessage("bot", botResponse);
    }
}