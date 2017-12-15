package com.cooksys.tweeter.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Client;
import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Integer>{

	Set<Tweet> findByHashtagsOrderByPosted(String hashtagName);

	Tweet findByIdAndDeleted(Integer id, boolean deleted);
	
	Tweet findById(Integer id);

	List<Tweet> findByinReplyToAndDeleted(Tweet tweet, boolean deleted);

	List<Tweet> findByAuthorAndDeleted(Client client, boolean deleted);

	List<Tweet> findByMentionedByAndDeleted(Client client, boolean deleted);

	List<Tweet> findByRepostOfAndDeleted(Tweet tweet, boolean deleted);

	List<Tweet> findByinReplyTo(Tweet current);

	List<Tweet> findByHashtagsAndDeleted(Hashtag tag, boolean deleted);
}
