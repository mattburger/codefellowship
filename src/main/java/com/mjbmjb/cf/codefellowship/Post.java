package com.mjbmjb.cf.codefellowship;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String username;
    String body;
    Date createdAt;

    @ManyToOne
    AppUser user;

    public Post(){}

    public Post(String body, AppUser user, String username){
        this.body = body;
        this.user = user;
        this.username = username;
        this.createdAt = new Date();

    }

    public String getBody() {
        return this.body;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public String getUsername() { return this.username; }
}
