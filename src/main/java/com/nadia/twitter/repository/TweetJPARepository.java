package com.nadia.twitter.repository;

import com.nadia.twitter.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetJPARepository extends JpaRepository<Tweet, Long> {
    @Query("select t from Tweet t where t.creator.id = :userId order by t.createdAt desc ")
    List<Tweet> getTweetsSortedByDateOFCreation(
            @Param("userId") Long userId);
}
