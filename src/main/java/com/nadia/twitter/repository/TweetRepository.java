package com.nadia.twitter.repository;

import com.nadia.twitter.model.Tweet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TweetRepository {


    private final List<Tweet> tweets = new ArrayList<>();
    private final UserRepository userRepository;

    public TweetRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void save(Tweet tweet) {
        tweets.add(tweet);
    }

    public List<Tweet> getAllActivesTweets() {
        return tweets.stream()
                .filter(tweet -> tweet.isDeleted() == false)
                .collect(Collectors.toList());
    }

    public List<Tweet> getTweetsSortedByDateOFCreation() {
        return tweets.stream()
                .sorted(Comparator.comparing(Tweet::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }


    public Tweet getTweetById(Long id) {
        return tweets.stream()
                .filter(tweet -> tweet.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new RuntimeException("no tweet fond"));
    }

    public List<Tweet> getTweetsByCreatorId(Long creator) {
        return tweets.stream()
                .filter(tweet -> tweet.getCreator().getId().equals(creator))
                .sorted(Comparator.comparing(Tweet::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public Tweet getTweetByLike(Long tweetId, Long userLikeId, Long userDisLikeId) {
        Tweet tweet = getTweetById(tweetId);
        tweet.addLike(userLikeId);
        tweet.addDisLike(userDisLikeId);
        return tweet;
    }

    public void clear() {
        tweets.clear();
    }
}
