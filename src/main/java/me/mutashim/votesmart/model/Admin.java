package me.mutashim.votesmart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin")
public class Admin {

    @Id
    private String id;
    private String username;
    private String password;

    // Constructor
    public Admin() {
        this.username = "admin";  // Default username
        this.password = "admin";  // Default password
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
