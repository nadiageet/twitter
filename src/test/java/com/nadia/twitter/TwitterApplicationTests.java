package com.nadia.twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadia.twitter.model.Tweet;
import com.nadia.twitter.repository.TweetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest // test d'integration
@AutoConfigureMockMvc
// @ExtendWith(MockitoExtension.class) a mettre sur un service
class TwitterApplicationTests {

    // H2 database
    // testcontainers image docker contenant une database
    // mock

    //    @Mock
    // @MockBean
    TweetRepository tweetRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllTweets() throws Exception {
        List<Tweet> tweets = tweetRepository.getAllTweets();
        System.out.println("tweets = " + tweets);

        mockMvc.perform(get("/url"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("qqch"));

        mockMvc.perform(
                        post("/url")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(tweets))
                )
                .andExpect(status().isAccepted());

    }

}
