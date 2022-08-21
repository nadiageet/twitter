package com.nadia.twitter.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID uuid = UUID.randomUUID();
    private Long id;
    private String userName;
    private List<User> followed = new ArrayList<>();
    private List<User> blockedUsers = new ArrayList<>();
    private boolean isInfluencer = false;


    public User(Long id, String userName) {
        this.id = id;
        this.userName = userName;
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
