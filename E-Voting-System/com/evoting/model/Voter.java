package com.evoting.model;

import java.io.Serializable;
import java.util.UUID;

public class Voter implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String passwordHash;
    private boolean hasVoted;

    public Voter(String name, String passwordHash) {
        this.id = UUID.randomUUID().toString().substring(0,8);
        this.name = name;
        this.passwordHash = passwordHash;
        this.hasVoted = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPasswordHash() { return passwordHash; }
    public boolean hasVoted() { return hasVoted; }
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }
}
