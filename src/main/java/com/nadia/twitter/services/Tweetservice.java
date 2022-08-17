package com.nadia.twitter.services;

import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Tweetservice {

    private final TweetRepository tweetRepository = new TweetRepository();
    private final UserRepository userRepository = new UserRepository();

    public void tweet(String content, Long creatorId) {

        Tweet tweet = tweetRepository.getAllTweets().
                stream()
                .filter(tweet1 -> tweet1.getContent().equals(content))
                .findAny()
                .orElseThrow(() -> new RuntimeException("aucun tweet avec ce contenu"));

        tweet.setCreator(userRepository.getUserById(creatorId));
        tweetRepository.save(tweet);

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
        return null;
    }
}
