package com.nadia.twitter.controller.request;

public class CreateTweetRequest {


    private String content;

    private Long requesterId;

    private Boolean isPrivate = false;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        if (aPrivate != null) {
            isPrivate = aPrivate;
        }
    }
}
