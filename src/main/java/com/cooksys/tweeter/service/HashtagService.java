package com.cooksys.tweeter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.repository.HashtagRepository;

@Service
public class HashtagService {

	HashtagRepository hashtagRepository;

	public HashtagService(HashtagRepository hashtagRepository) {
		super();
		this.hashtagRepository = hashtagRepository;
	}
	
	public Hashtag create(String hashtagName){
		Hashtag tag = new Hashtag(hashtagName);
		return hashtagRepository.save(tag);
	}
	
	public boolean tagExists(String label) {
		List<Hashtag> hashtags = hashtagRepository.findByHashtagName(label);
		return hashtags.size() > 0;
	}
}
