package com.nadia.twitter.repository;

import com.nadia.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository extends JpaRepository<User, Long> {

}
