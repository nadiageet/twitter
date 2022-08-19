package com.nadia.twitter.services;

import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class TweetserviceTest {
    public static final long NADIA_ID = 1L;
    public static final long GUIGUI_ID = 2L;
    public static final long SOFIA_ID = 3L;
    UserRepository userRepository = new UserRepository();
    TweetRepository tweetRepository = new TweetRepository(userRepository);
    Tweetservice tweetservice = new Tweetservice(tweetRepository, userRepository);
    private User soso;
    private User nadia;
    private User guigui;


    @BeforeEach
    void setUp() {
        nadia = new User(NADIA_ID, "nadia");
        guigui = new User(2L, "guigui");
        soso = new User(3L, "sofia");
        Arrays.asList(
                nadia,
                guigui,
                soso
        ).forEach(userRepository::save);

//        Tweet tweet1 = new Tweet(1L, "la météo", nadia, LocalDateTime.now());
//        Tweet tweet2 =new Tweet(2L, "les vacances", guigui, LocalDateTime.parse("2022-08-12T10:15:30"));
//        Tweet tweet3 =new Tweet(3L, "la rentrée", soso, LocalDateTime.parse("2022-08-15T10:15:30"));
//        Tweet tweet4 =new Tweet(4L, "la politique", nadia, LocalDateTime.parse("2022-08-10T10:15:30"));
//        Arrays.asList(
//                tweet1, tweet2, tweet3, tweet4
//        ).forEach(tweetRepository::save);
    }

    @Test
    void getTweetsByCreatorId() {

        // GIVEN
        Tweet tweetNadia1 = tweetservice.tweet("le repas du soir", NADIA_ID);
        Tweet tweetNadia2 = tweetservice.tweet("le repas de minuit", NADIA_ID);
        Tweet tweetNadia3 = tweetservice.tweet("le repas du midi", NADIA_ID);

        tweetservice.tweet("le repas du matin", 2L);
        tweetservice.tweet("le repas du lyna", 3L);

        // WHEN
        List<Tweet> tweetsNadia = tweetservice.getTweetsOfUser(NADIA_ID);

        // THEN
        Assertions.assertThat(tweetsNadia)
                .hasSize(3)
                .containsExactlyInAnyOrder(tweetNadia1, tweetNadia2, tweetNadia3);

    }

    @Test
    void getUserFeed() {
        Tweet tweetNadia1 = tweetservice.tweet("le repas du soir", SOFIA_ID);
        Tweet tweetNadia2 = tweetservice.tweet("le repas de minuit", SOFIA_ID);
        Tweet tweetNadia3 = tweetservice.tweet("le repas du midi", GUIGUI_ID);
        Tweet tweetNadia4 = tweetservice.tweet("le repas du soir", NADIA_ID);
        Tweet tweetNadia5 = tweetservice.tweet("le repas de minuit", GUIGUI_ID);
        Tweet tweetNadia6 = tweetservice.tweet("le repas du midi", GUIGUI_ID);
        tweetservice.follow(GUIGUI_ID, NADIA_ID);
        tweetservice.follow(SOFIA_ID, NADIA_ID);

        List<Tweet> tweets = tweetservice.getUserFeed(NADIA_ID);
        Assertions.assertThat(tweets)
                .hasSize(5);

    }

    @Test
    void getFeed() {
        for (int i = 0; i < 20; i++) {
            Tweet tweet = new Tweet((long) i, "le repas du soir", soso, LocalDateTime.now());
            tweetRepository.save(tweet);
        }
        Tweet tweetNadia1 = tweetservice.tweet("le repas du soir", SOFIA_ID);
        Tweet tweetNadia2 = tweetservice.tweet("le repas de minuit", SOFIA_ID);
        Tweet tweetNadia3 = tweetservice.tweet("le repas du midi", GUIGUI_ID);
        Tweet tweetNadia4 = tweetservice.tweet("le repas du soir", NADIA_ID);
        Tweet tweetNadia5 = tweetservice.tweet("le repas de minuit", GUIGUI_ID);
        Tweet tweetNadia6 = tweetservice.tweet("le repas du midi", GUIGUI_ID);

//            0                   1                   2           3
//        [x, x, x, x, x || x, x, x, x, x || x, x, x, x, x || x, x, x, x, x, x, x, x, x, ]
        List<Tweet> tweets = tweetservice.getFeed(NADIA_ID, 1, 5);
        System.out.println("tweets = " + tweets);
        Assertions.assertThat(tweets)
                .hasSize(5);

    }

    @Test
    void creatPrivateTweet() {

        String content = "le repas du midi";
        Tweet tweet = tweetservice.creatPrivateTweet(content, NADIA_ID);
        Assertions.assertThat(tweet.isPrivate())
                .isTrue();

        Assertions.assertThat(tweet.getContent())
                .isEqualTo(content);

    }

    @Test
    void follow() {

        long creatorId = 1L;
        User creator = userRepository.getUserById(creatorId);
        long blockedId = 2L;
        User blocked = userRepository.getUserById(blockedId);
        tweetservice.blockfollow(creatorId, blockedId);
        assertThat(blocked.getBlockedUsers().size()).isEqualTo(1);
        AssertionsForInterfaceTypes.assertThat(blocked.getBlockedUsers()).contains(creator);


    }

    @Test
    @DisplayName("Should update recent tweet")
    void updateTweet() {
        Tweet tweet = new Tweet(10L, "ce que tu veux", nadia, LocalDateTime.now());
        tweetRepository.save(tweet);

        tweetservice.updateTweet(tweet.getId(), "update tweet");
        assertThat(tweet.getContent()).isEqualTo("update tweet");
    }

    @Test
    @DisplayName("Should not update old tweet")
    void updateOldTweet() {
        Tweet tweet = new Tweet(10L, "ce que tu veux", nadia, LocalDateTime.now().minus(5, ChronoUnit.HOURS));
        tweetRepository.save(tweet);

        Throwable throwable = catchThrowable(() -> tweetservice.updateTweet(tweet.getId(), "update tweet"));

        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("the tweet can not be codified");

        assertThat(tweet.getContent()).isEqualTo("ce que tu veux");
    }

    @Test
    void deleteTweetByCreator() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.deleteTweet(tweet.getId(), nadia.getId());

        // THEN
        assertThat(tweetRepository.getTweetById(1L).isDeleted()).isTrue();
    }

    @Test
    void deleteTweetByOtherUser() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        Throwable throwable = catchThrowable(() -> tweetservice.deleteTweet(tweet.getId(), nadia.getId() + 1));

        assertThat(throwable).isNotNull();

        // THEN
        assertThat(tweetRepository.getTweetById(1L).isDeleted()).isFalse();
    }

    @Test
    void likeTweetByCreator() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.likeTweet(tweet.getId(), nadia.getId());

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getLikes()).isEqualTo(1);
    }

    @Test
    void likeTweetByOther() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.likeTweet(tweet.getId(), nadia.getId() + 1);

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getLikes()).isEqualTo(1);
    }

    @Test
    void likeTweetByTwoPeople() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.likeTweet(tweet.getId(), nadia.getId());
        tweetservice.likeTweet(tweet.getId(), nadia.getId() + 1);

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getLikes()).isEqualTo(2);
    }

    @Test
    void likeTweetTwiceBySamePerson() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.likeTweet(tweet.getId(), nadia.getId());
        tweetservice.likeTweet(tweet.getId(), nadia.getId());

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getLikes()).isEqualTo(1);
    }


}