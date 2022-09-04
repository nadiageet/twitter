package com.nadia.twitter.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    @ManyToMany
    private Set<User> followed = new HashSet<>();
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

    public Set<User> getFollowed() {
        return followed;
    }

    public List<User> getBlockedUsers() {
        return blockedUsers;
    }

    public void setFollowed(Set<User> followed) {
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

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
