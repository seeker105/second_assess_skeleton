package com.cooksys.tweeter.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Integer>{

	Set<Tweet> findByHashtagsOrderByPostedDesc(String hashtagName);

	Set<Tweet> findAllOrderByPostedDesc();

	Tweet findByIdAndDeleted(Integer id, boolean deleted);

}
