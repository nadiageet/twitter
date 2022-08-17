package com.nadia.twitter.controller.request;

public class UserRequest {

    private Long UserId;


    public UserRequest(Long userId) {
        UserId = userId;
    }


    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

}
