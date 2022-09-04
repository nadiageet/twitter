package com.nadia.twitter;

import com.nadia.twitter.model.Tweet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
//        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Transactional
public class ApplicationIT {

    @Autowired
    EntityManager entityManager;

    @Test
    void shouldStart() {

        Tweet tweet = new Tweet();
        tweet.setContent("Hello, World!");
        entityManager.persist(tweet);

        assertThat(tweet.getId()).isNotNull();
    }
}
