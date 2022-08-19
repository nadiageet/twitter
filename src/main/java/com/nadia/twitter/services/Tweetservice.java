package com.nadia.twitter.services;

import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Tweetservice {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public Tweetservice(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public Tweet tweet(String content, Long creatorId) {

        Tweet tweet = new Tweet(content, userRepository.getUserById(creatorId), LocalDateTime.now());
        tweetRepository.save(tweet);
        return tweet;

    }

    public Tweet creatPrivateTweet(String content, Long creatorId) {

        Tweet tweet = tweet(content, creatorId);
        tweet.setPrivate(true);
        return tweet;

    }

    public List<Tweet> getTweetsOfUser(Long userId) {

        return tweetRepository.getTweetsSortedByDateOFCreation()
                .stream()
                .filter(tweet -> tweet.getCreator().getId().equals(userId))
                .collect(Collectors.toList());

    }

    public void follow(Long creatorId, Long followerId) {
        User creator = userRepository.getUserById(creatorId);
        User follower = userRepository.getUserById(followerId);

        follower.follow(creator);
    }

    public void blockfollow(Long creatorId, Long userBlockedId) {
        User creator = userRepository.getUserById(creatorId);
        User userBlocked = userRepository.getUserById(userBlockedId);
        userBlocked.blockFollow(creator);
    }

    public List<Tweet> getUserFeed(Long requesterId) {
//        Map<Long, List<Tweet>> tweetsMap = new HashMap<>();

        User user = userRepository.getUserById(requesterId);
        List<Long> followrsId = user.getFollowed().stream()
                .map(user1 -> user1.getId())
                .collect(Collectors.toList());
//
//        for(Long id : followrsId){
//            tweetsMap.put(id, getTweetsOfUser(id));
//        }

        return tweetRepository.getTweetsSortedByDateOFCreation().stream()
                .filter(tweet -> followrsId.contains(tweet.getCreator().getId()))
                .collect(Collectors.toList());
//        return tweetsMap.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .map(entry -> entry.getValue())
//                .findAny()
//                .orElseThrow(() -> new RuntimeException());
    }

    public List<Tweet> getFeed(Long requesterId, Integer page, Integer pageSize) {
        List<Tweet> tweets = tweetRepository.getAllActivesTweets()
                .stream()
                .skip((long) page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        return tweets;

    }


    public void updateTweet(Long tweetId, String tweetContent) {

        Tweet tweet = tweetRepository.getTweetById(tweetId);
        long hoursFromCreation = tweet.getCreatedAt().until(LocalDateTime.now(), ChronoUnit.HOURS);
        if (hoursFromCreation >= 1) {
            throw new IllegalArgumentException("the tweet can not be codified");
        }
        tweet.setContent(tweetContent);
        tweet.setUpdatedAt(LocalDateTime.now());


    }

    void deleteTweet(Long tweetId, Long requesterId) {
        Tweet tweet = tweetRepository.getTweetById(tweetId);
        if (!(tweet.getCreator().equals(userRepository.getUserById(requesterId)))) {
            throw new IllegalArgumentException("the tweet can not be deleted");
        }
        tweet.setDeleted(true);
    }

    void likeTweet(Long tweetId, Long requesterId) {
        Tweet tweet = tweetRepository.getTweetById(tweetId);
        tweet.addLike(requesterId);
    }


}
