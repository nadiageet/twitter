package com.nadia.twitter.job.reader;

import com.nadia.twitter.feign.RandomUserClient;
import com.nadia.twitter.feign.model.RandomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;

//@Component
@RequiredArgsConstructor
public class RandomUserReader implements ItemReader<RandomUser.Root> {

    private static int count = 0;
    private final RandomUserClient randomUserClient;

    @Override
    public RandomUser.Root read() {
        if (count++ < 10) {
            return randomUserClient.getUser();
        } else {
            count = 0;
            return null;
        }
    }
}
