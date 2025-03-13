package org.hiss.repositories;

import org.hiss.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM tweet WHERE userid = :userID")
    List<Tweet> findByUserID(Long userID);
}
