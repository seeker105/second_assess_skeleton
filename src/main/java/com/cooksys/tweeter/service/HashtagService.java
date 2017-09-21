package com.cooksys.tweeter.service;

import java.util.Set;

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
		Hashtag hashtag = hashtagRepository.findByHashtagName(label);
		return hashtag != null;
	}
	
}
