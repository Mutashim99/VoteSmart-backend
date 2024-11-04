package me.mutashim.votesmart.model;

public class Candidate {

    private String id; // Unique identifier for the candidate
    private String name;
    private int age;
    private String description;
    private int voteCount;

    // Constructors
    public Candidate() {}

    public Candidate(String id, String name, int age, String description, int voteCount) {
        this.id = id; // Initialize the ID
        this.name = name;
        this.age = age;
        this.description = description;
        this.voteCount = voteCount;
    }

    // Getters and Setters
    public String getId() {
        return id; // Getter for ID
    }

    public void setId(String id) {
        this.id = id; // Setter for ID
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
