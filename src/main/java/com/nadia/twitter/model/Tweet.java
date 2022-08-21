package com.nadia.twitter.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Tweet {

    private Long id;
    private String content;
    private User creator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private boolean isPrivate = false;


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

    private boolean isDeleted = false;

    private int likes = 0;
    private int dislikes = 0;

    Set<Long> likesByUserIds = new HashSet<>();
    Set<Long> dislikesByUserIds = new HashSet<>();


    public void setCreator(User creator) {
        this.creator = creator;
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
        return likesByUserIds.size();
    }

    public int getDislikes() {
        return dislikesByUserIds.size();
    }

    public void addLike(Long userId) {
        likesByUserIds.add(userId);
    }

    public void addDisLike(Long userId) {
        dislikesByUserIds.add(userId);
    }


    public int getNetLikes() {
        return likesByUserIds.size() - dislikesByUserIds.size();
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
