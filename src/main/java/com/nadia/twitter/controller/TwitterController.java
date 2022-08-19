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
        return tweetRepository.getAllActivesTweets();
    }

    @GetMapping("/tweets/user/{userId}")
    public List<Tweet> gettweetById(@PathVariable("userId") Long userId) {
        return tweetservice.getTweetsOfUser(userId);
    }

    @PostMapping("/tweets")
    public void tweet(@RequestBody UserRequest userRequest, TweetRequest tweetRequest) {
        tweetservice.tweet(tweetRequest.getContent(), userRequest.getUserId());
    }

    @GetMapping("/tweets/{userId}")
    private List<Tweet> getTweetsOfUser(@PathVariable("userId") Long userId) {
        return tweetservice.getTweetsOfUser(userId);
    }

    @GetMapping("/tweets")
    public List<Tweet> getFeed(@RequestParam("requesterId") Long requesterId) {
        return tweetservice.getFeed(requesterId, 0, 10);
    }

    @PutMapping("tweet/{tweetId}")
    public void updateTwwet(@PathVariable("tweetId") Long tweetId, @RequestParam("tweetContent") String tweetContent) {
        Tweet tweet = tweetRepository.getTweetById(tweetId);
        tweetservice.updateTweet(tweetId, tweetContent);


    }

}
