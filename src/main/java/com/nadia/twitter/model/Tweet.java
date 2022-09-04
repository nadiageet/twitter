package com.nadia.twitter.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Column(name = "private", nullable = false)
    private boolean isPrivate = false;


    private boolean isDeleted = false;
    @ManyToMany
    Set<User> likesByUsers = new HashSet<>();

    @ManyToMany
    Set<User> dislikesByUsers = new HashSet<>();


    public Tweet() {

    }

    public Tweet(Long id, String content, User creator, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.creator = creator;
        this.createdAt = createdAt;
    }

    public Tweet(String content, User creator, LocalDateTime createdAt) {
        this.content = content;
        this.creator = creator;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getCreator() {
        return creator;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getLikes() {
        return likesByUsers.size();
    }

    public int getDislikes() {
        return dislikesByUsers.size();
    }

    public void addLike(User user) {
        likesByUsers.add(user);
    }

    public void addDisLike(User user) {
        dislikesByUsers.add(user);
    }


    public int getNetLikes() {
        return likesByUsers.size() - dislikesByUsers.size();
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", creator=" + creator +
                ", createdAt=" + createdAt +
                '}';
    }


}
