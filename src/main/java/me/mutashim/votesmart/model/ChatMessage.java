package me.mutashim.votesmart.model;


import java.util.Queue;

public class ChatMessage {
    private String sender; // "user" or "bot"
    private String content;
    Queue<String> msgQueue;


    public ChatMessage() {
    }

    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
    public ChatMessage(String sender, String content, Queue<String> msgQueue) {
        this.sender = sender;
        this.content = content;
        this.msgQueue = msgQueue;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
