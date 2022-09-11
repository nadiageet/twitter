package com.nadia.twitter.job.processor;

import com.nadia.twitter.feign.RandomUserClient;
import com.nadia.twitter.feign.model.RandomUser;
import com.nadia.twitter.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RandomUserProcessor implements ItemProcessor<RandomUser.Root, User> {

    private final RandomUserClient randomUserClient;

    @Override
    public User process(RandomUser.Root root) {
        User user = new User();
        user.setUserName(root.results.get(0).login.username);
        log.info("processing user {}", user.getUserName());
        return user;
    }
}
