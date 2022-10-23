package com.nadia.twitter.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
@EnableScheduling
@Slf4j
public class GlobalConfiguration {

    @Scheduled(fixedRate = 1_000L)
    public void test() {
        log.info("test called...");
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Paris"));
    }
}
