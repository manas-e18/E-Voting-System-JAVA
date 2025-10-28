package com.evoting.model;

import java.io.Serializable;
import java.util.UUID;

public class Candidate implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;

    public Candidate(String name) {
        this.id = UUID.randomUUID().toString().substring(0,8);
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String toString() { return name + " ("+id + ")"; }
}
