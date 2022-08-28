package com.nadia.twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.TweetRepository;
import com.nadia.twitter.repository.UserRepository;
import com.nadia.twitter.services.TweetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest // test d'integration
@AutoConfigureMockMvc
// @ExtendWith(MockitoExtension.class) a mettre sur un service
class TwitterApplicationTests {

    // H2 database
    // testcontainers image docker contenant une database
    // mock

    @Autowired
    UserRepository userRepository;
    @Autowired
    TweetRepository tweetRepository;
    @Autowired
    TweetService tweetservice;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2022-08-01T14:41:00Z"), ZoneId.of("Europe/Paris"));

        User nadia = new User(1L, "nadia");
        User guigui = new User(2L, "guigui");
        User soso = new User(3L, "sofia");
        List<User> users = Arrays.asList(
                nadia,
                guigui,
                soso
        );
        List<Tweet> tweets =
                Arrays.asList(
                        new Tweet(1L, "la météo", nadia, LocalDateTime.now(clock)),
                        new Tweet(2L, "les vacances", guigui, LocalDateTime.parse("2022-08-12T10:15:30")),
                        new Tweet(3L, "la rentrée", soso, LocalDateTime.parse("2022-08-15T10:15:30")),
                        new Tweet(4L, "la politique", nadia, LocalDateTime.parse("2022-08-10T10:15:30"))
                );

        tweets.forEach(tweetRepository::save);
        users.forEach(userRepository::save);

    }

    @AfterEach
    void after() {
        userRepository.clear();
        tweetRepository.clear();
    }


    @Test
    void getAllTweets() throws Exception {
        List<Tweet> tweets = tweetRepository.getAllActivesTweets();

        //  System.out.println("tweets = " + tweets);

        assertThat(tweets).hasSize(4);

        String json = mockMvc.perform(get("/tweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(4)))
                .andReturn().getResponse()
                .getContentAsString();

        System.out.println("json = " + json);

        List<Tweet> content = Arrays.asList(
                objectMapper.readValue(json, Tweet[].class)
        );

        assertThat(content)
                .hasSize(4)
                .extracting(Tweet::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L);

//                .andExpect(jsonPath("$[*].id", containsInAnyOrder("1", "2", "3", "4")));
        //  .andExpect(jsonPath("$.content", hasSize(4)));

        /*mockMvc.perform(
                        post("/url")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(tweets))
                )
                .andExpect(status().isAccepted());*/

    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users = userRepository.getAllUsers();
        //  System.out.println("tweets = " + tweets);

        assertThat(users).hasSize(3);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
        //  .andExpect(jsonPath("$.content", hasSize(3)));

    }

    @Test
    void getTweetById() throws Exception {

        User nadia = userRepository.getUserById(1L);

        mockMvc.perform(get("/tweets/user/{userId}", nadia.getId()))
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasSize(2)));
    }

    @Test
    void createTweet() {

        Tweet tweet = tweetservice.tweet("tweet test", 1L);
        assertThat(tweet.getCreator().getId()).isEqualTo(1L);

    }

    @Test
    void getTweetsOfUser() throws Exception {

        List<Tweet> tweets = tweetservice.getTweetsOfUser(1L);
        assertThat(tweets.size()).isEqualTo(2);

        mockMvc.perform(get("/tweets/{userId}", 1L))
                .andExpect(status().isOk());
        // .andExpect(jsonPath("$.content", hasSize(2)));

    }

    @Test
    void follow() {

        long creatorId = 1L;
        User creator = userRepository.getUserById(creatorId);
        long followerId = 2L;
        User follower = userRepository.getUserById(followerId);


        tweetservice.follow(creatorId, followerId);

        assertThat(follower.getFollowed().size()).isEqualTo(1);
        assertThat(follower.getFollowed()).contains(creator);


    }

    @Test
    void getUserFeed() {
        long creatorId = 1L;
        User creator = userRepository.getUserById(creatorId);
        long followerId = 2L;
        User follower = userRepository.getUserById(followerId);

        long creatorId2 = 3L;
        User creator2 = userRepository.getUserById(creatorId2);


        tweetservice.follow(creatorId, followerId);
        tweetservice.follow(creatorId2, followerId);

        assertThat(follower.getFollowed().size()).isEqualTo(2);
        assertThat(follower.getFollowed()).contains(creator);
        assertThat(follower.getFollowed()).contains(creator2);

    }

    @Test
    void updateTweet() throws Exception {
        Tweet tweet = tweetservice.tweet("tweet test", 10L);
        tweetservice.updateTweet(tweet.getId(), "update tweet");
        mockMvc.perform(get("tweet/{tweetId", tweet.getContent()))
                // THEN
                .andExpect(status().isOk());


    }


}
