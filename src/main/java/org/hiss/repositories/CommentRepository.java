package org.hiss.repositories;

import org.hiss.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE tweetid = :tweetID")
    List<Comment> findByTweetID(Long tweetID);
}
