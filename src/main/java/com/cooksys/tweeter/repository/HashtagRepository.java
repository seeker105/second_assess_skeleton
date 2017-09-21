package com.cooksys.tweeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Hashtag;


public interface HashtagRepository extends JpaRepository<Hashtag, Integer>{
	
	Hashtag findByHashtagName(String hashtagName);

}
