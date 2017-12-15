package com.cooksys.tweeter.service;

import org.springframework.stereotype.Service;

@Service
public class ValidateService {
	
	private HashtagService hashtagService;

	public ValidateService(HashtagService hashtagService) {
		super();
		this.hashtagService = hashtagService;
	}

	public boolean tagExists(String label) {
		return hashtagService.tagExists(label);
	}

}
