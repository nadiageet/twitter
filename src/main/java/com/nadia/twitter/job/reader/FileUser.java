package com.nadia.twitter.job.reader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUser {

    private String userName;
    private String influencer;

    public FileUser() {
    }

    public FileUser(String userName, boolean influencer) {
        this.userName = userName;
        this.influencer = Boolean.toString(influencer);
    }
}
