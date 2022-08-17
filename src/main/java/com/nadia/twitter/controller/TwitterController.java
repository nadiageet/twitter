package com.nadia.twitter.controller;

import com.nadia.twitter.controller.request.TweetRequest;
import com.nadia.twitter.controller.request.UserRequest;
import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.services.Tweetservice;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TwitterController {

    private final TweetRepository tweetRepository;
    private final Tweetservice tweetservice;

    @GetMapping("/tweets")
    public List<Tweet> getAllTweets() {
        return tweetRepository.getAllTweets();
    }

    @PostMapping("/tweet")
    public void tweet(@RequestBody UserRequest userRequest, TweetRequest tweetRequest) {
        tweetservice.tweet(tweetRequest.getContent(), userRequest.getUserId());
    }

    @GetMapping("/tweets/{userId}")
    private List<Tweet> getTweetsOfUser(@PathVariable("userId") Long userId) {
        return tweetservice.getTweetsOfUser(userId);
    }

}
