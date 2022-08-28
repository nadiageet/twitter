package com.nadia.twitter.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class GlobalConfiguration {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Paris"));
    }
}
