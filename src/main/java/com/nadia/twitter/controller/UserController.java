package com.nadia.twitter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadia.twitter.controller.request.CreateUserRequest;
import com.nadia.twitter.feign.RandomUserClient;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserJPARepository userRepository;
    private final RandomUserClient randomUserClient;
    private final ObjectMapper objectMapper;

    private final JobLauncher jobLauncher;

    private final Job createRandomUserJob;

    private final Clock clock;


    @GetMapping("/users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUserName(request.getUserName());
        return userRepository.save(user);
    }

    @GetMapping("/users/random")
    public String getUser() {
        return randomUserClient.getUserJson();
    }

    @PostMapping("/users/random")
    public void createRandomUser() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(createRandomUserJob,
                new JobParametersBuilder()
                        .addLong("timestamp",
                                System.currentTimeMillis())
                        .toJobParameters());
    }

    private String getUserNameFromClass() {
        return randomUserClient.getUser().results.get(0).login.username;
    }

    private String getUserNameFromJson() throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(randomUserClient.getUserJson());

        JsonNode results = root.get("results").get(0);
        JsonNode login = results.get("login");
        JsonNode username = login.get("username");
        String userName = username.asText();
        return userName;
    }
}
