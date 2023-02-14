package com.nadia.twitter.job;

import com.nadia.twitter.job.reader.FileUser;
import com.nadia.twitter.job.writer.UserDatabaseWriter;
import com.nadia.twitter.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableBatchProcessing
@Slf4j
//@RequiredArgsConstructor
public class ImportUser {


    public static final String[] NAMES = new String[]{"userName", "influencer"};
    private final JobBuilderFactory jobs;

    private final StepBuilderFactory steps;

    public ImportUser(JobBuilderFactory jobs, StepBuilderFactory steps) {
        this.jobs = jobs;
        this.steps = steps;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<FileUser> userFileReader(@Value("#{jobParameters['file_path']}") String filePath) {

        log.info("reader created with file {}", filePath);
        return new FlatFileItemReaderBuilder<FileUser>()
                .name("userFileReader")
                .resource(new PathResource(filePath))
                // skip the header
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names(NAMES)
                .targetType(FileUser.class)
                .build();
    }

    @Bean
    public ItemProcessor<FileUser, User> userFileProcessor() {
        return fileUser -> {
            User user = new User();
            user.setUserName(fileUser.getUserName());
            user.setInfluencer(Boolean.parseBoolean(fileUser.getInfluencer()));
            return user;
        };

    }

    @Bean
    public Step creatFileUserStep(
            FlatFileItemReader<FileUser> userFileReader,
            ItemProcessor<FileUser, User> userFileProcessor,
            UserDatabaseWriter userDatabaseWriter
    ) {
        return steps.get("createFileUser")
                .<FileUser, User>chunk(10)
                .reader(userFileReader)
                .processor(userFileProcessor)
                .writer(userDatabaseWriter)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet deleteFileTasklet(@Value("#{jobParameters['file_path']}") String filePath) {
        return (contribution, chunkContext) -> {
            try {
                Files.delete(Path.of(filePath));
                log.info("file {} was deleted", filePath);
            } catch (IOException e) {
                log.error("Error while deleting file {}", filePath, e);
            }
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step deleteFileStep(Tasklet deleteFileTasklet) {
        return steps.get("deleteFileStep")
                .tasklet(deleteFileTasklet)
                .build();
    }

    @Bean
    public Job fileUserJob(
            Step creatFileUserStep,
            Step deleteFileStep
    ) {
        log.info("file user job starting");
        return jobs.get("createFileUserJob")
                .start(creatFileUserStep)
                .next(deleteFileStep)
                .build();
    }
}
