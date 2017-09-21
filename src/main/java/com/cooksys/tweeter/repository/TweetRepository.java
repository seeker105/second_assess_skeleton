package com.cooksys.tweeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Integer>{

}
