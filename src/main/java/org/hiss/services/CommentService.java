package org.hiss.services;

import org.hiss.DTO.CommentDTO;
import org.hiss.entities.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(CommentDTO commentDTO, Long tweetID);
    Boolean deleteComment(Long commentID);
    Boolean editComment(Long commentID, CommentDTO commentDTO);
    Boolean likeComment(Long commentID);
    List<Comment> getCommentsByTweetID(Long tweetID);
    Comment getCommentByID(Long commentID);
}
