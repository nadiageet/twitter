package com.nadia.twitter.job.writer;

import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDatabaseWriter implements ItemWriter<User> {
    private final UserJPARepository userRepository;

    @Override
    public void write(List<? extends User> users) throws Exception {

        log.info("saving {} users", users.size());
        users.forEach(userRepository::save);
    }
}
