package com.cooksys.tweeter.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Integer>{

	Set<Tweet> findByHashtagsOrderByPosted(String hashtagName);

	Tweet findByIdAndDeleted(Integer id, boolean deleted);
	
	Tweet findById(Integer id);

}
