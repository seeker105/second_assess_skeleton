package com.cooksys.tweeter.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.repository.HashtagRepository;

@Service
public class ValidateService {
	
	private HashtagRepository hashtagRepository;
	private HashtagService hashtagService;
	


	public ValidateService(HashtagRepository hashtagRepository, HashtagService hashtagService) {
		super();
		this.hashtagRepository = hashtagRepository;
		this.hashtagService = hashtagService;
	}



	public boolean tagExists(String label) {
		return hashtagService.tagExists(label);
	}

}
