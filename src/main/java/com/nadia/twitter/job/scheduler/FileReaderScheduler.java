package com.nadia.twitter.job.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
@EnableScheduling
public class FileReaderScheduler {

    private final Job fileUserJob;

    private final JobLauncher jobLauncher;


    public FileReaderScheduler(Job fileUserJob, JobLauncher jobLauncher) {
        this.fileUserJob = fileUserJob;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(cron = "-")
//    @Scheduled(cron = "*/5 * * * * *")
    public void startUserFileJob() throws Exception {
        log.info("scheduler tick");
        String filePath = "/var/users.csv";
        if (Files.exists(Path.of(filePath))) {
            JobParameters parameters = new JobParametersBuilder()
                    .addString("file_path", filePath)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(fileUserJob, parameters);
        } else {
            log.info("file {} does not exist, job was not started", filePath);
        }
    }
}
