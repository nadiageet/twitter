package com.nadia.twitter.model;

import java.time.LocalDateTime;

public class Tweet {

    private Long id;
    private String content;
    private User creator;
    private LocalDateTime createdAt;


    public Tweet(Long id, String content, User creator, LocalDateTime createdAt) {
        this.id = id;
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

    public void setCreator(User creator) {
        this.creator = creator;
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
