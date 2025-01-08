package me.mutashim.votesmart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Document(collection = "polls")
public class Poll {

    @Id
    private String id;
    private String title;
    private String description;
    private LinkedList<Candidate> candidates;
    private String creatorId;
    private boolean approved;
    private String pollType; // Added field for poll type: general or domain-specific
    private String allowedDomain; // Added field for domain (only applicable for domain-specific polls)

    public Poll() {}

    public Poll(String title, String description, LinkedList<Candidate> candidates, String creatorId, String pollType, String allowedDomain) {
        this.title = title;
        this.description = description;
        this.candidates = candidates;
        this.creatorId = creatorId;
        this.pollType = pollType;
        this.allowedDomain = allowedDomain;
    }

    // Getter and Setter methods
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public LinkedList<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(LinkedList<Candidate> candidates) {
        this.candidates = candidates;
    }

    public String getPollType() {
        return pollType;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public String getAllowedDomain() {
        return allowedDomain;
    }

    public void setAllowedDomain(String allowedDomain) {
        this.allowedDomain = allowedDomain;
    }
}
