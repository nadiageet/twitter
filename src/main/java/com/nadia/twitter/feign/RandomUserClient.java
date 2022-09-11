package com.nadia.twitter.feign;

import com.nadia.twitter.feign.model.RandomUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "randomUser",
        url = "https://randomuser.me"
)
public interface RandomUserClient {

    @GetMapping(value = "/api")
    String getUserJson();

    @GetMapping(value = "/api")
    RandomUser.Root getUser();

}
