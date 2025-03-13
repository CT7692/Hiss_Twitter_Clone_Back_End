package org.hiss.controllers;

import org.apache.coyote.Request;
import org.hiss.DTO.CommentDTO;
import org.hiss.DTO.PicDTO;
import org.hiss.DTO.TweetDTO;
import org.hiss.DTO.TweetLikesDTO;
import org.hiss.entities.Comment;
import org.hiss.entities.Tweet;
import org.hiss.entities.User;
import org.hiss.response.RequestResponse;
import org.hiss.services.CommentService;
import org.hiss.services.TweetService;
import org.hiss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    //########################## USER SERVICES ##########################

    @GetMapping("/user_info")
    public ResponseEntity<User> getCurrentUserInfo(Principal principal) {
        User user = userService.getUserInfo(principal.getName());
        if(user == null)
            return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        User user = userService.getUserInfo(name);
        if(user == null)
            return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(user);
    }

    //########################## COMMENT SERVICES ##########################

    @PostMapping("/tweet/{tweetID}/comment")
    public ResponseEntity<Comment> createComment(@PathVariable Long tweetID,
            @RequestBody CommentDTO commentDTO){
        Comment comment = commentService.createComment(commentDTO, tweetID);

        if(comment != null)
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tweet/{tweetID}/comments")
    public ResponseEntity<List<Comment>> getCommentsByTweetID(@PathVariable Long tweetID){
        Tweet tweet = tweetService.getTweetByID(tweetID);
        if(tweet == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(commentService.getCommentsByTweetID(tweetID));
    }

    @PostMapping("/comment/{commentID}/edit")
    public ResponseEntity<RequestResponse> editComment(
            @PathVariable Long commentID,
            @RequestBody CommentDTO commentDTO){

        boolean editSuccessful = commentService.editComment(commentID, commentDTO);

        if(editSuccessful)
            return ResponseEntity.ok(new RequestResponse(true));
        else
            return new ResponseEntity<>(new RequestResponse(false), HttpStatus.NOT_FOUND);
    }

    @PostMapping("comment/{commentID}/like")
    public ResponseEntity<RequestResponse> likeComment(@PathVariable Long commentID){
        boolean success = commentService.likeComment(commentID);

        if(success)
            return ResponseEntity.ok(new RequestResponse(true));
        else
            return new ResponseEntity<>(
                    new RequestResponse(false), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete_comment/{commentID}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long commentID){
        boolean success = commentService.deleteComment(commentID);

        if(success)
            return ResponseEntity.ok(true);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/comment/{commentID}")
    public ResponseEntity<Comment> getCommentByID(@PathVariable Long commentID){
        Comment comment = commentService.getCommentByID(commentID);

        if(comment == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(comment);
    }

    //########################## TWEET SERVICES ##########################

    @GetMapping("/tweets")
    public ResponseEntity<List<Tweet>> getTweets() {
        return ResponseEntity.ok(tweetService.getTweets());
    }

    @PostMapping("/tweet")
    public ResponseEntity<Tweet> createTweet(@RequestBody TweetDTO tweetDTO) {
        Tweet tweet = tweetService.createTweet(tweetDTO);

        if(tweet != null)
            return new ResponseEntity<>(tweet, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/tweet/{tweetID}")
    public ResponseEntity<Tweet> getTweetByID(@PathVariable Long tweetID) {
        Tweet tweet = tweetService.getTweetByID(tweetID);

        if(tweet == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(tweet);
    }

    @PostMapping("edit_tweet/{tweetID}")
    public ResponseEntity<RequestResponse> editTweet(
            @PathVariable Long tweetID,
            @RequestBody TweetDTO tweetDTO) {

        boolean editSuccessful = tweetService.editTweet(tweetID, tweetDTO);

        if(editSuccessful)
            return ResponseEntity.ok(new RequestResponse(true));
        else
            return new ResponseEntity<>(new RequestResponse(false), HttpStatus.NOT_FOUND);
    }

    @PostMapping("tweet/{tweetID}/like")
    public ResponseEntity<RequestResponse> likeTweet(@PathVariable Long tweetID) {
        boolean success = tweetService.likeTweet(tweetID);

        if(success)
            return ResponseEntity.ok(new RequestResponse(true));
        else
            return new ResponseEntity<>(
                    new RequestResponse(false), HttpStatus.NOT_FOUND);

    }

    @GetMapping("tweet/{tweetID}/likes")
    public ResponseEntity<TweetLikesDTO> getTweetLikes(@PathVariable Long tweetID) {
        Integer tweetLikes = tweetService.getTweetLikes(tweetID);
        if(tweetLikes == null)
            return new ResponseEntity<>(new TweetLikesDTO(-1), HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(new TweetLikesDTO(tweetLikes));
    }

    @GetMapping("/tweets/{name}")
    public ResponseEntity<List<Tweet>> getTweetsByUser(@PathVariable String name) {
        Long userID = userService.getUserIDFromUsername(name);
        if(userID == null)
            return new ResponseEntity<>(List.of(null), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(tweetService.getTweetsByUserID(userID));
    }

    @DeleteMapping("/delete_tweet/{tweetID}")
    public ResponseEntity<Boolean> deleteTweet(@PathVariable Long tweetID) {
        boolean success = tweetService.deleteTweet(tweetID);

        if(success)
            return ResponseEntity.ok(true);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update_pic")
    public ResponseEntity<RequestResponse> updatePic(
            Principal principal, @RequestBody PicDTO picDTO) {

        boolean success = userService.changeImage(principal.getName(), picDTO);

        if(success)
            return ResponseEntity.ok(new RequestResponse(true));
        else
            return new ResponseEntity<>(new
                    RequestResponse(false), HttpStatus.NOT_FOUND);
    }

}
