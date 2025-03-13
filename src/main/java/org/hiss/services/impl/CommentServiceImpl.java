package org.hiss.services.impl;

import org.hiss.DTO.CommentDTO;
import org.hiss.entities.Comment;
import org.hiss.entities.Tweet;
import org.hiss.entities.User;
import org.hiss.exceptions.ResourceNotFoundException;
import org.hiss.repositories.CommentRepository;
import org.hiss.repositories.TweetRepository;
import org.hiss.repositories.UserRepository;
import org.hiss.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(CommentDTO commentDTO, Long tweetID) {

        Comment comment = null;
        Tweet tweet = tweetRepository.findById(tweetID)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet", "Tweet ID", String.valueOf(tweetID)));

        User user = userRepository.findByName(commentDTO.getName());
        if(user == null)
            throw new ResourceNotFoundException("User", "Username", commentDTO.getName());
        else{
            comment = new Comment();
            comment.setContent(commentDTO.getContent());
            comment.setTime(new Date(System.currentTimeMillis()));
            comment.setLikeCount(0);
            comment.setLikedByCurrentUser(false);
            comment.setTweet(tweet);
            comment.setUser(user);
            commentRepository.save(comment);
        }

        return comment;
    }

    @Override
    public Boolean deleteComment(Long commentID) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment ID", String.valueOf(commentID)));
        commentRepository.delete(comment);
        return true;
    }

    @Override
    public Boolean editComment(Long commentID, CommentDTO commentDTO) {
        boolean success = false;
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment ID", String.valueOf(commentID)));
        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);
        success = true;
        return success;
    }

    @Override
    public Boolean likeComment(Long commentID) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment", "Comment ID", String.valueOf(commentID)));

        if(!comment.getLikedByCurrentUser()){
            comment.setLikedByCurrentUser(true);
            int likes = comment.getLikeCount();
            comment.setLikeCount(likes + 1);
        }
        else{
            comment.setLikedByCurrentUser(false);
            int likes = comment.getLikeCount();
            comment.setLikeCount(likes - 1);
        }
        commentRepository.save(comment);
        return true;
    }

    @Override
    public List<Comment> getCommentsByTweetID(Long tweetID) {
        return commentRepository.findByTweetID(tweetID);
    }

    @Override
    public Comment getCommentByID(Long commentID) {
        return commentRepository.findById(commentID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment", "Comment ID", String.valueOf(commentID)));
    }
}
