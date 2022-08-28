package com.nadia.twitter.controller;

import com.nadia.twitter.controller.request.CreateTweetRequest;
import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.services.TweetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/tweets")
public class TwitterController {

    private final TweetRepository tweetRepository;
    private final TweetService tweetservice;

    @GetMapping("/tweets")
    public List<Tweet> getAllTweets(@RequestParam(value = "requesterId", required = false) Long requesterId) {
        if (requesterId == null) {
            return tweetRepository.getAllActivesTweets();
        }
        return tweetservice.getFeed(requesterId, 0, 10);
    }

    @GetMapping("/tweets/user/{userId}")
    public List<Tweet> getTweetsByUserId(@PathVariable("userId") Long userId) {
        return tweetservice.getTweetsOfUser(userId);
    }

    @ApiResponse(responseCode = "201")
    @Operation(description = "créer un tweet par un utilisateur")
    @PostMapping("/tweets")
    public void createTweet(@RequestBody CreateTweetRequest request) {
        tweetservice.tweet(request.getContent(), request.getRequesterId());
        if (request.getPrivate()) {
            tweetservice.creatPrivateTweet(request.getContent(), request.getRequesterId());
        }
    }

    @GetMapping("/tweets/{userId}")
    private List<Tweet> getTweetsOfUser(
            @Parameter(description = "l'identifiant du requester", example = "123456")
            @PathVariable("userId") Long userId) {
        return tweetservice.getTweetsOfUser(userId);
    }

    @PostMapping("/follow")
    private void followTweets(@RequestParam("requesterId") Long requesterId, @RequestParam("creatorId") Long creatorId) {
        tweetservice.follow(creatorId, requesterId);
    }

    @PostMapping("/block")
    private void blockfollow(@RequestParam("requesterId") Long requesterId, @RequestParam("useToBlockId") Long useToBlockId) {
        tweetservice.blockfollow(requesterId, useToBlockId);
    }

    @GetMapping("/tweets/feed")
    public List<Tweet> getUserFeed(@RequestParam("requesterId") Long requesterId) {
        return tweetservice.getUserFeed(requesterId);
    }

    @PutMapping("/tweets/{tweetId}")
    public void updateTweet(@PathVariable("tweetId") Long tweetId, @RequestParam("tweetContent") String tweetContent) {
//        Tweet tweet = tweetRepository.getTweetById(tweetId);
        tweetservice.updateTweet(tweetId, tweetContent);
    }

    @DeleteMapping("/tweets/{tweetId}")
    public void deleteTweet(@PathVariable("tweetId") Long tweetId, @RequestParam("requesterId") Long requesterId) {
        tweetservice.deleteTweet(tweetId, requesterId);
    }

    @PostMapping("/tweets/{tweetId}/like")
    public void likeTweet(@PathVariable("tweetId") Long tweetId, @RequestParam("requesterId") Long requesterId) {
        tweetservice.likeTweet(tweetId, requesterId);
    }

    @PostMapping("/tweets/{tweetId}/dislike")
    public void dislikeTweet(@PathVariable("tweetId") Long tweetId, @RequestParam("requesterId") Long requesterId) {
        tweetservice.dislikeTweet(tweetId, requesterId);
    }

    @GetMapping("/tweets/hot")
    public List<Tweet> getHottestTweets(@RequestParam("requesterId") Long requesterId) {
        return tweetservice.getHottestTweets(requesterId);
    }


}
