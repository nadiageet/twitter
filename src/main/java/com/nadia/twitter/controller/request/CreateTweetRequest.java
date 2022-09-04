package com.nadia.twitter.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateTweetRequest {

    @NotBlank
    private String content;

    @NotNull
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
