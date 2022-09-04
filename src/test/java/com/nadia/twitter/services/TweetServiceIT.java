package com.nadia.twitter.services;

import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.TweetJPARepository;
import com.nadia.twitter.repository.UserJPARepository;
import com.nadia.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    @Autowired
    TweetJPARepository tweetJPARepository;


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

    @Test
    void blockfollow() {

        // GIVEN
        User creator = new User();
        creator.setUserName("nadia");
        User userBlocked = new User();
        userBlocked.setUserName("guigui");

        userJPARepository.save(creator);
        userJPARepository.save(userBlocked);
        // WHEN
        tweetService.blockfollow(creator.getId(), userBlocked.getId());
        // THEN
        assertThat(creator.getBlockedUsers()).containsOnlyOnce(userBlocked);

    }

    @Test
    void getTweetForFollowUser() {

        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");

        User guigui = new User();
        guigui.setUserName("guigui");

        User sofia = new User();
        sofia.setUserName("sofia");

        userJPARepository.save(nadia);
        userJPARepository.save(guigui);
        userJPARepository.save(sofia);
        tweetService.follow(nadia.getId(), guigui.getId());
        tweetService.follow(sofia.getId(), guigui.getId());

        tweetService.tweet("tweet nadia", nadia.getId());
        tweetService.tweet("tweet sofia", sofia.getId());
        tweetService.tweet("tweet nadia 1", nadia.getId());
        tweetService.tweet("tweet nadia 2", nadia.getId());

        // WHEN
        List<Tweet> userFeed = tweetService.getUserFeed(guigui.getId());

        //THEN
        assertThat(userFeed).hasSize(4);


    }

    @Test
    void updateTweet() {
        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        userJPARepository.save(nadia);

        Tweet tweetNadia = tweetService.tweet("tweet nadia", nadia.getId());

        // WHEN
        tweetService.updateTweet(tweetNadia.getId(), "toujours pour moi");

        //THEN
        assertThat(tweetNadia.getContent()).isEqualTo("toujours pour moi");

    }

    @Test
    void deleteTweet() {
        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        userJPARepository.save(nadia);

        Tweet tweetNadia1 = tweetService.tweet("tweet nadia deux", nadia.getId());
        // WHEN
        tweetService.deleteTweet(tweetNadia1.getId(), nadia.getId());

        //THEN
        assertThat(tweetNadia1.isDeleted()).isTrue();
    }

    @Test
    void likeTweet() {

        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        userJPARepository.save(nadia);
        Tweet tweetNadia1 = tweetService.tweet("tweet nadia deux", nadia.getId());

        // WHEN
        tweetService.likeTweet(tweetNadia1.getId(), nadia.getId());

        //THEN
        assertThat(tweetNadia1.getLikes()).isEqualTo(1);
    }

    @Test
    void dislikeTweet() {

        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");
        userJPARepository.save(nadia);
        Tweet tweetNadia1 = tweetService.tweet("tweet nadia deux", nadia.getId());

        // WHEN
        tweetService.dislikeTweet(tweetNadia1.getId(), nadia.getId());

        //THEN
        assertThat(tweetNadia1.getDislikes()).isEqualTo(1);
    }

    @Test
    void getFeed() {


        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");

        User guigui = new User();
        guigui.setUserName("guigui");

        User sofia = new User();
        sofia.setUserName("sofia");

        userJPARepository.save(nadia);
        userJPARepository.save(guigui);
        userJPARepository.save(sofia);

        for (int i = 0; i < 20; i++) {
            Tweet tweet = new Tweet((long) i, "le repas du soir", nadia, LocalDateTime.now());
            tweetService.tweet(tweet.getContent(), nadia.getId());
        }
        tweetService.tweet("tweet.getContent()", nadia.getId());
        tweetService.tweet("tweet.getContent()", nadia.getId());
        tweetService.tweet("tweet.getContent()", nadia.getId());
        tweetService.tweet("tweet.getContent()", nadia.getId());
        tweetService.tweet("tweet.getContent()", nadia.getId());
        tweetService.tweet("tweet.getContent()", nadia.getId());

//            0                   1                   2           3
//        [x, x, x, x, x || x, x, x, x, x || x, x, x, x, x || x, x, x, x, x, x, x, x, x, ]
        Page<Tweet> tweets = tweetService.getFeed(nadia.getId(), PageRequest.of(3, 5));
        System.out.println("tweets = " + tweets);
        assertThat(tweets).hasSize(5);

    }

    @Test
    void getHottestTweets() {

        // GIVEN
        User nadia = new User();
        nadia.setUserName("nadia");

        User guigui = new User();
        guigui.setUserName("guigui");

        User sofia = new User();
        sofia.setUserName("sofia");

        userJPARepository.save(nadia);
        userJPARepository.save(guigui);
        userJPARepository.save(sofia);

        for (int i = 0; i < 20; i++) {
            Tweet tweet = tweetService.tweet("tweet.getContent(", nadia.getId());
            tweetService.likeTweet(tweet.getId(), sofia.getId());
            tweetService.likeTweet(tweet.getId(), guigui.getId());
        }

        List<Tweet> tweets = tweetService.getHottestTweets(sofia.getId(), PageRequest.of(0, 100));

        assertThat(tweets)
                .hasSize(20);

    }
}
