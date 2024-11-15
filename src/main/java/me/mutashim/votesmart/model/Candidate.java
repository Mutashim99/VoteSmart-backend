package me.mutashim.votesmart.model;

public class Candidate {

    private String id;
    private String name;
    private int age;
    private String description;
    private int voteCount;


    public Candidate() {}

    public Candidate(String id, String name, int age, String description, int voteCount) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.description = description;
        this.voteCount = voteCount;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
