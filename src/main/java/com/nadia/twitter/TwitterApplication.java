package com.nadia.twitter;

import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TwitterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TwitterApplication.class, args);

        UserRepository bean = context.getBean(UserRepository.class);

        bean.save(new User(0L, "toto"));
    }

}
