package me.mutashim.votesmart.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Stack<String> pollIds;


    public User() {}

    public User(String name, String email, String password, Stack<String> pollIds) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.pollIds = pollIds;
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

    public Stack<String> getPollIds() {
        return pollIds;
    }

    public void setPollIds(Stack<String> pollIds) {
        this.pollIds = pollIds;
    }
}

