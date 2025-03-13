package org.hiss.services;

import org.hiss.DTO.TweetDTO;
import org.hiss.entities.Tweet;

import java.util.List;

public interface TweetService {
    List<Tweet> getTweets();
    Tweet createTweet(TweetDTO tweetDTO);
    Tweet getTweetByID(Long tweetID);
    Boolean deleteTweet(Long tweetID);
    Boolean editTweet(Long tweetID, TweetDTO tweetDTO);
    Boolean likeTweet(Long tweetID);
    Integer getTweetLikes(Long tweetID);
    List<Tweet> getTweetsByUserID(Long userID);
}
