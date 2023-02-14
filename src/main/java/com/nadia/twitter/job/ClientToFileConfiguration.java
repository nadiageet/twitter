package com.nadia.twitter.job;

import com.nadia.twitter.feign.model.RandomUser;
import com.nadia.twitter.job.reader.FileUser;
import com.nadia.twitter.job.reader.RandomUserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class ClientToFileConfiguration {
    private final JobBuilderFactory jobs;

    private final StepBuilderFactory steps;


    @Bean
    public Step saveRandomUserToFileStep(
            RandomUserReader reader,
            ItemProcessor<RandomUser.Root, FileUser> processCreateFileUser,
            FlatFileItemWriter<FileUser> flatFileItemWriter
    ) {
        return steps.get("saveRandomUserToFileStep")
                .<RandomUser.Root, FileUser>chunk(3)
                .reader(reader)
                .processor(processCreateFileUser)
                .writer(flatFileItemWriter)
                .build();
    }

    @Bean
    public ItemProcessor<RandomUser.Root, FileUser> processCreateFileUser() {
        return root -> {
            RandomUser.Name name = root.results.get(0).name;
            return new FileUser(name.first + " " + name.last, false);
        };
    }

    @Bean
    public FlatFileItemWriter<FileUser> flatFileItemWriter() {
        return new FlatFileItemWriterBuilder<FileUser>()
                .headerCallback(writer -> writer.write("""
                        "userName","influencer" \
                        """))
                .name("randomUserFileWriter")
                .resource(new PathResource("/var/users.csv"))
                .delimited().delimiter(",")
                .names("userName", "influencer")
                .build();
    }

    @Bean
    public Job importToFileJob(Step saveRandomUserToFileStep) {
        return jobs.get("importToFile")
                .start(saveRandomUserToFileStep)
                .build();
    }


}
