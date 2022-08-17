package com.nadia.twitter.repository;

import com.nadia.twitter.model.Tweet;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TweetRepository {

    public final List<Tweet> tweets;
    private final UserRepository userRepository = new UserRepository();

    public TweetRepository() {
        tweets = Arrays.asList(
                new Tweet(1L, "la météo", userRepository.getUserById(1L), LocalDateTime.now()),
                new Tweet(2L, "les vacances", userRepository.getUserById(2L), LocalDateTime.parse("2022-08-12T10:15:30")),
                new Tweet(3L, "la rentrée", userRepository.getUserById(3L), LocalDateTime.parse("2022-08-15T10:15:30")),
                new Tweet(4L, "la politique", userRepository.getUserById(1L), LocalDateTime.parse("2022-08-10T10:15:30"))
        );
    }


    public void save(Tweet tweet) {
        tweets.add(tweet);
    }

    public List<Tweet> getAllTweets() {
        return tweets;
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
}
