package com.nadia.twitter.job;

import com.nadia.twitter.feign.RandomUserClient;
import com.nadia.twitter.feign.model.RandomUser;
import com.nadia.twitter.job.processor.RandomUserProcessor;
import com.nadia.twitter.job.reader.RandomUserReader;
import com.nadia.twitter.job.writer.UserDatabaseWriter;
import com.nadia.twitter.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class RandomUserJobConfiguration {

    private final JobBuilderFactory jobs;

    private final StepBuilderFactory steps;

    // définir un reader

    // définir le processor RandomUser.Root => User

    // définir le writer qui va sauvegarder les user

    // definir step

    @Bean
    @StepScope
    RandomUserReader randomUserReader(RandomUserClient randomUserClient) {
        return new RandomUserReader(randomUserClient);
    }

    @Bean
    public Step createRandomUserStep(
            RandomUserReader reader,
            RandomUserProcessor processor,
            UserDatabaseWriter writer
    ) {
        return steps.get("createRandomUserStep")
                .<RandomUser.Root, User>chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // definir job

    @Bean
    public Job createRandomUserJob(
            Step createRandomUserStep
    ) {
        return jobs.get("createRandomUserJob")
                .start(createRandomUserStep)
                .build();
    }

    // definir un scheduler ou une api pour démarrer le job
}
