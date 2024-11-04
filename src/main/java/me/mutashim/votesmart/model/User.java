package me.mutashim.votesmart.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> pollIds;  // List of IDs for polls created by this user

    // Constructors
    public User() {}

    public User(String name, String email, String password, List<String> pollIds) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.pollIds = pollIds;
    }

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getPollIds() {
        return pollIds;
    }

    public void setPollIds(List<String> pollIds) {
        this.pollIds = pollIds;
    }
}

