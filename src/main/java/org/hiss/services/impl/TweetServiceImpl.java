package org.hiss.services.impl;

import org.hiss.DTO.TweetDTO;
import org.hiss.entities.Tweet;
import org.hiss.entities.User;
import org.hiss.exceptions.ResourceNotFoundException;
import org.hiss.exceptions.TweetTooLongException;
import org.hiss.repositories.TweetRepository;
import org.hiss.repositories.UserRepository;
import org.hiss.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Tweet> getTweets() {
        return tweetRepository.findAll();
    }

    @Override
    public Tweet createTweet(TweetDTO tweetDTO) {
        Tweet tweet = null;

        User user = userRepository.findByName(tweetDTO.getName());
        if(user == null)
            throw new ResourceNotFoundException("User", "Username", tweetDTO.getName());
        else {
            if(tweetDTO.getContent().length() > 144)
                throw new TweetTooLongException("Hiss cannot have over 144 characters.");
            else {
                tweet = new Tweet();
                tweet.setContent(tweetDTO.getContent());
                tweet.setTime(new Date(System.currentTimeMillis()));
                tweet.setLikeCount(0);
                tweet.setLikedByCurrentUser(false);
                tweet.setUser(user);
                tweetRepository.save(tweet);
            }
        }
       return tweet;
    }

    @Override
    public Boolean deleteTweet(Long tweetID) {
        Tweet tweet = tweetRepository.findById(tweetID)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet", "Tweet ID", String.valueOf(tweetID)));
        tweetRepository.delete(tweet);
        return true;
    }

    @Override
    public Boolean editTweet(Long tweetID, TweetDTO tweetDTO) {
        boolean success = false;
        Tweet tweet = tweetRepository.findById(tweetID)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet", "Tweet ID", String.valueOf(tweetID)));
        tweet.setContent(tweetDTO.getContent());
        tweetRepository.save(tweet);
        success = true;
        return success;
    }

    @Override
    public List<Tweet> getTweetsByUserID(Long userID) {
        return tweetRepository.findByUserID(userID);
    }

    @Override
    public Tweet getTweetByID(Long tweetID) {
        return tweetRepository.findById(tweetID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tweet", "Tweet ID", String.valueOf(tweetID)));
    }

    @Override
    public Integer getTweetLikes(Long tweetID) {
        Tweet tweet = tweetRepository.findById(tweetID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tweet", "Tweet ID", String.valueOf(tweetID)));
        return tweet.getLikeCount();
    }

    @Override
    public Boolean likeTweet(Long tweetID) {
        Tweet tweet = tweetRepository.findById(tweetID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tweet", "Tweet ID", String.valueOf(tweetID)));

        if(!tweet.getLikedByCurrentUser()) {
            tweet.setLikedByCurrentUser(true);
            int likes = tweet.getLikeCount();
            tweet.setLikeCount(likes + 1);
        }
        else{
            tweet.setLikedByCurrentUser(false);
            int likes = tweet.getLikeCount();
            tweet.setLikeCount(likes - 1);
        }
        tweetRepository.save(tweet);
        return true;
    }

}
