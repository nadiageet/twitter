package com.nadia.twitter.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    @ManyToMany
    private List<User> followed = new ArrayList<>();
    @ManyToMany
    private List<User> blockedUsers = new ArrayList<>();

    @Column(name = "influencer", nullable = false)
    private boolean isInfluencer = false;


    public User(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public List<User> getFollowed() {
        return followed;
    }

    public List<User> getBlockedUsers() {
        return blockedUsers;
    }

    public void setFollowed(List<User> followed) {
        this.followed = followed;
    }

    public void follow(User user) {
        followed.add(user);
    }

    public void blockFollow(User user) {
        blockedUsers.add(user);
    }

    public boolean isInfluencer() {
        return isInfluencer;
    }

    public void setInfluencer(boolean influencer) {
        isInfluencer = influencer;
    }
}
