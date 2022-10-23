package com.nadia.twitter.controller.response;

import com.nadia.twitter.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private String userName;

    private boolean isInfluencer = false;

    public UserResponse(User user) {
        this.userName = user.getUserName();
        this.isInfluencer = user.isInfluencer();
    }

}
