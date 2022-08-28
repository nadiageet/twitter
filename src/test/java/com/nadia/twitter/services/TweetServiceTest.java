package com.nadia.twitter.services;

import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class TweetServiceTest {
    public static final long NADIA_ID = 1L;
    public static final long GUIGUI_ID = 2L;
    public static final long SOFIA_ID = 3L;
    UserRepository userRepository = new UserRepository();
    TweetRepository tweetRepository = new TweetRepository(userRepository);

    Clock clock = Clock.fixed(Instant.parse("2022-08-01T14:41:00Z"), ZoneId.of("Europe/Paris"));
    TweetService tweetservice = new TweetService(tweetRepository, userRepository, clock);
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
        assertThat(tweetsNadia)
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
        assertThat(tweets)
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
        assertThat(tweets)
                .hasSize(5);

    }

    @Test
    void creatPrivateTweet() {

        String content = "le repas du midi";
        Tweet tweet = tweetservice.creatPrivateTweet(content, NADIA_ID);
        assertThat(tweet.isPrivate())
                .isTrue();

        assertThat(tweet.getContent())
                .isEqualTo(content);

    }

    @Test
    void block() {

        long creatorId = 1L;
        User creator = userRepository.getUserById(creatorId);
        long blockedId = 2L;
        tweetservice.blockfollow(creatorId, blockedId);

        assertThat(creator.getBlockedUsers()).hasSize(1);

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

    @Test
    void disLikeTweetByCreator() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.dislikeTweet(tweet.getId(), nadia.getId());

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getDislikes()).isEqualTo(1);
    }

    @Test
    void disLikeTweetByOther() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.dislikeTweet(tweet.getId(), nadia.getId() + 1);

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getDislikes()).isEqualTo(1);
    }

    @Test
    void disLikeTweetByTwoPeople() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.dislikeTweet(tweet.getId(), nadia.getId());
        tweetservice.dislikeTweet(tweet.getId(), nadia.getId() + 1);

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getDislikes()).isEqualTo(2);
    }

    @Test
    void disLikeTweetTwiceBySamePerson() {
        // GIVEN
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.dislikeTweet(tweet.getId(), nadia.getId());
        tweetservice.dislikeTweet(tweet.getId(), nadia.getId());

        // THEN
        assertThat(tweetRepository.getTweetById(1L).getDislikes()).isEqualTo(1);
    }

    @Test
    void getTweetByLike() {
        Tweet tweet = new Tweet(1L, "la météo", nadia, LocalDateTime.now());

        tweetRepository.save(tweet);
        // WHEN
        tweetservice.likeTweet(tweet.getId(), nadia.getId());
        tweetservice.dislikeTweet(tweet.getId(), nadia.getId() + 1);

        tweetRepository.getTweetByLike(1L, nadia.getId(), nadia.getId() + 1);
        assertThat(tweetRepository.getTweetById(1L).getLikes()).isEqualTo(1);
        assertThat(tweetRepository.getTweetById(1L).getDislikes()).isEqualTo(1);
    }

    @Test
    void getHottestTweets() {
        for (int i = 0; i < 20; i++) {
            Tweet tweet = new Tweet((long) i, "le repas du soir", soso, LocalDateTime.now());
            tweetRepository.save(tweet);
        }
        tweetservice.likeTweet(1L, NADIA_ID);
        tweetservice.likeTweet(1L, GUIGUI_ID);
        tweetservice.likeTweet(1L, SOFIA_ID);
        tweetservice.dislikeTweet(1L, NADIA_ID);

        tweetservice.likeTweet(2L, NADIA_ID);
        tweetservice.likeTweet(2L, GUIGUI_ID);
        tweetservice.dislikeTweet(2L, GUIGUI_ID);


        List<Tweet> tweets = tweetservice.getHottestTweets(3L);
        assertThat(tweets)
                .hasSize(20)
                .first().isEqualTo(tweetRepository.getTweetById(1L));
        assertThat(tweets.get(1))
                .isEqualTo(tweetRepository.getTweetById(2L));

    }

    @Test
    void influencer() {
        Tweet tweet = new Tweet(1L, "le repas du soir", nadia, LocalDateTime.now());
        tweetRepository.save(tweet);

        for (int i = 1; i <= 110; i++) {
            User user = new User(NADIA_ID + i, "likeTweet");
            tweetservice.likeTweet(1L, user.getId());
        }
        assertThat(tweet.getLikes()).isEqualTo(110);
        assertThat(tweet.getCreator()).isEqualTo(nadia);
        assertThat(userRepository.getUserById(NADIA_ID).isInfluencer())
                .isTrue();
    }

    @Test
    void priorInfluencer() {

        nadia.setInfluencer(true);
        soso.setInfluencer(true);
        Tweet tweetNadia = new Tweet(1L, "le repas du soir", nadia, LocalDateTime.now());
        Tweet tweetSofia = new Tweet(2L, "le repas du soir", soso, LocalDateTime.now());
        tweetRepository.save(tweetNadia);
        tweetRepository.save(tweetSofia);

        Tweet tweetGuigui1 = new Tweet(3L, "de guigui", guigui, LocalDateTime.now());
        Tweet tweetGuigui2 = new Tweet(4L, "de guigui", guigui, LocalDateTime.now());
        Tweet tweetGuigui3 = new Tweet(5L, "de guigui", guigui, LocalDateTime.now());

        tweetRepository.save(tweetGuigui1);
        tweetRepository.save(tweetGuigui2);
        tweetRepository.save(tweetGuigui3);
        tweetservice.likeTweet(4L, nadia.getId());

        User toto = new User(42L, "toto");

        List<Tweet> feed = tweetservice.getHottestTweets(toto.getId());
        assertThat(feed).hasSize(5);
        assertThat(feed).containsExactly(
                tweetSofia, tweetNadia, tweetGuigui2, tweetGuigui1, tweetGuigui3
        );

        System.out.println(feed);


    }

}