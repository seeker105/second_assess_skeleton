package com.cooksys.tweeter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.repository.HashtagRepository;

@Service
public class ValidateService {
	
	private HashtagRepository hashtagRepository;
	
	public ValidateService(HashtagRepository hashtagRepository) {
		super();
		this.hashtagRepository = hashtagRepository;
	}

	public boolean tagExists(String label) {
		List<Hashtag> hashtags = hashtagRepository.findByHashtagName(label);
		return hashtags.size() > 0;
	}

}
