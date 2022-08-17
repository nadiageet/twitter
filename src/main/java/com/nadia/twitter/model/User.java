package com.nadia.twitter.model;


import java.util.List;

public class User {

    private Long id;
    private String userName;
    private List<User> followed;

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

    public void setFollowed(List<User> followed) {
        this.followed = followed;
    }

    public void follow(User user) {
        followed.add(user);
    }
}
