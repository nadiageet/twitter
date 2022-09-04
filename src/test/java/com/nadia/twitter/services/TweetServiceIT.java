package com.nadia.twitter.services;

import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.UserJPARepository;
import com.nadia.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TweetServiceIT {

    @Autowired
    TweetService tweetService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserJPARepository userJPARepository;

    @Test
    void shouldCreateTweet() {
        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        userJPARepository.save(nadia);
        // WHEN
        Tweet tweetNadia = tweetService.tweet("tweet nadia", nadia.getId());
        // THEN
        assertThat(tweetNadia.getCreator()).isEqualTo(nadia);
    }

    @Test
    void shouldCreatePrivateTweet() {
        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        userJPARepository.save(nadia);
        // WHEN
        Tweet tweetNadia = tweetService.creatPrivateTweet("tweet nadia", nadia.getId());
        // THEN
        assertThat(tweetNadia.isPrivate()).isTrue();
    }

    @Test
    void shouldGetTweetOfUser() {
        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        User guigui = new User();
        nadia.setUserName("guigui");
        userJPARepository.save(nadia);
        userJPARepository.save(guigui);
        Tweet tweetNadia = tweetService.creatPrivateTweet("tweet nadia", nadia.getId());
        Tweet tweetGuigui = tweetService.creatPrivateTweet("tweet guigui", guigui.getId());
        Tweet tweetNadia2 = tweetService.creatPrivateTweet("tweet nadia 1", nadia.getId());
        Tweet tweetNadia3 = tweetService.creatPrivateTweet("tweet nadia 2", nadia.getId());
        // WHEN
        List<Tweet> tweetsNadia = tweetService.getTweetsOfUser(nadia.getId());
        // THEN
        assertThat(tweetsNadia.size()).isEqualTo(3);
    }

    @Test
    void followOncePerUser() {

        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        User guigui = new User();
        nadia.setUserName("guigui");

        userJPARepository.save(nadia);
        userJPARepository.save(guigui);
        // WHEN
        tweetService.follow(nadia.getId(), guigui.getId());
        tweetService.follow(nadia.getId(), guigui.getId());
        // THEN
        assertThat(guigui.getFollowed()).containsOnlyOnce(nadia);


    }
}
