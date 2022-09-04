package com.nadia.twitter.services;

import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.TweetJPARepository;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.repository.UserJPARepository;
import com.nadia.twitter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    private final TweetJPARepository tweetJPARepository;

    private final UserJPARepository userJPARepository;

    private final Clock clock;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository, TweetJPARepository tweetJPARepository, UserJPARepository userJPARepository, Clock clock) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.tweetJPARepository = tweetJPARepository;
        this.userJPARepository = userJPARepository;
        this.clock = clock;
    }

    private static int sortByInfluencerThenNetLikes(Tweet left, Tweet right) {
        if (left.getCreator().isInfluencer()) {
            return -1;
        }
        if (right.getCreator().isInfluencer()) {
            return 1;
        }
        return right.getNetLikes() - left.getNetLikes();
    }

    public Tweet tweet(String content, Long creatorId) {
        Tweet tweet = new Tweet(content, userJPARepository.getReferenceById(creatorId), LocalDateTime.now(clock));
        tweetJPARepository.save(tweet);
        return tweet;
    }

    public Tweet creatPrivateTweet(String content, Long creatorId) {

        Tweet tweet = tweet(content, creatorId);
        tweet.setPrivate(true);
        return tweet;

    }

    public List<Tweet> getTweetsOfUser(Long userId) {
        return tweetJPARepository.getTweetsByUserIdSortedByDateOFCreation(userId);
    }

    public void follow(Long creatorId, Long followerId) {
        User creator = userJPARepository.getReferenceById(creatorId);
        User follower = userJPARepository.getReferenceById(followerId);

        follower.follow(creator);
    }

    public void blockfollow(Long requester, Long useToBlockId) {
        User creator = userJPARepository.getReferenceById(requester);
        User userBlocked = userJPARepository.getReferenceById(useToBlockId);
        creator.blockFollow(userBlocked);
    }

    public List<Tweet> getUserFeed(Long requesterId) {
//        Map<Long, List<Tweet>> tweetsMap = new HashMap<>();

        return tweetJPARepository.getUserFeed(requesterId);
//        User user = userJPARepository.getReferenceById(requesterId);
//        List<Long> followrsId = user.getFollowed().stream()
//                .map(User::getId)
//                .toList();
//
//        return tweetJPARepository.getTweetsByUserIds(followrsId);
//
//        for(Long id : followrsId){
//            tweetsMap.put(id, getTweetsOfUser(id));
//        }
//        return followrsId.stream()
//                 [1, 2, 3] ids
//                .map(tweetJPARepository::getTweetsByUserIdSortedByDateOFCreation)
//                 [[12, 13] , [3], []]
//                .flatMap(List::stream)
//                 [12, 13 , 3]
//                .toList();


//        return tweetJPARepository.getTweetsByUserIdSortedByDateOFCreation().stream()
//                .filter(tweet -> followrsId.contains(tweet.getCreator().getId()))
//                .collect(Collectors.toList());
//        return tweetsMap.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .map(entry -> entry.getValue())
//                .findAny()
//                .orElseThrow(() -> new RuntimeException());
    }

    public Page<Tweet> getFeed(Long requesterId, Pageable pageable) {
        return tweetJPARepository.getAllActivesTweets(pageable);
    }


    public void updateTweet(Long tweetId, String tweetContent) {

        Tweet tweet = tweetJPARepository.getReferenceById(tweetId);
        long hoursFromCreation = tweet.getCreatedAt().until(LocalDateTime.now(clock), ChronoUnit.HOURS);
        if (hoursFromCreation >= 1) {
            throw new IllegalArgumentException("the tweet can not be codified");
        }
        tweet.setContent(tweetContent);
        tweet.setUpdatedAt(LocalDateTime.now(clock));
    }

    public void deleteTweet(Long tweetId, Long requesterId) {
        Tweet tweet = tweetJPARepository.getReferenceById(tweetId);
        if (!(tweet.getCreator().getId().equals(requesterId))) {
            throw new IllegalArgumentException("the tweet can not be deleted");
        }
        tweet.setDeleted(true);
    }

    public void likeTweet(Long tweetId, Long requesterId) {
        Tweet tweet = tweetJPARepository.getReferenceById(tweetId);
        User user = userJPARepository.getReferenceById(requesterId);
        tweet.addLike(user);
        if (!tweet.getCreator().isInfluencer()) {
            userBeInfluencerForLive(tweet);
        }

    }

    private void userBeInfluencerForLive(Tweet tweet) {
        int sumTweet = tweetRepository.getTweetsByCreatorId(tweet.getCreator().getId())
                .stream()
                .mapToInt(Tweet::getLikes)
                .sum();
        if (sumTweet >= 100) {
            tweet.getCreator().setInfluencer(true);
        }
    }

    public void dislikeTweet(Long tweetId, Long requesterId) {
        Tweet tweet = tweetJPARepository.getReferenceById(tweetId);
        User user = userJPARepository.getReferenceById(requesterId);
        tweet.addDisLike(user);

    }

    public List<Tweet> getHottestTweets(Long requesterId, Pageable pageable) {
        List<Tweet> tweets = new ArrayList<>();
        for (Tweet tweet : tweetJPARepository.getAllActivesTweets(pageable)) {
            long tweetsForLastDay = tweet.getCreatedAt().until(LocalDateTime.now(clock), ChronoUnit.HOURS);
            if (tweetsForLastDay <= 24) {
                userBeInfluencerForLive(tweet);
                tweets.add(tweet);
            }

        }
        return tweets.stream()
                .sorted(TweetService::sortByInfluencerThenNetLikes)
                .collect(Collectors.toList());
    }

}
