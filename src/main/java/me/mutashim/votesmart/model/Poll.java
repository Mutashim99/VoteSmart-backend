package me.mutashim.votesmart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "polls")
public class Poll {

    @Id
    private String id;
    private String title;
    private String description;
    private List<Candidate> candidates;

    // Constructors
    public Poll() {}

    public Poll(String title, String description, List<Candidate> candidates) {
        this.title = title;
        this.description = description;
        this.candidates = candidates;
    }

    // Getters and Setters
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

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}