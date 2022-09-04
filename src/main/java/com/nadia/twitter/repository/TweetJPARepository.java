package com.nadia.twitter.repository;

import com.nadia.twitter.model.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetJPARepository extends JpaRepository<Tweet, Long> {
    @Query("select t from Tweet t where t.creator.id = :userId order by t.createdAt desc ")
    List<Tweet> getTweetsByUserIdSortedByDateOFCreation(@Param("userId") Long userId);

    @Query("select t from Tweet t where t.creator.id in (:userIds) order by t.createdAt desc")
    List<Tweet> getTweetsByUserIds(@Param("userIds") List<Long> userIds);

    @Query(
            "select t from Tweet t where t.creator.id in " +
            "(select f.id from User u join u.followed f where u.id=:userId) " +
            " order by t.createdAt desc"
    )
    List<Tweet> getUserFeed(@Param("userId") Long userId);

    @Query("select t from Tweet t where t.isDeleted=false ")
    Page<Tweet> getAllActivesTweets(Pageable pageable);

}
