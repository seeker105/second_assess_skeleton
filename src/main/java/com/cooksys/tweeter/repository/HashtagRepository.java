package com.cooksys.tweeter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Hashtag;


public interface HashtagRepository extends JpaRepository<Hashtag, Integer>{
	
	List<Hashtag> findByHashtagName(String hashtagName);

}
