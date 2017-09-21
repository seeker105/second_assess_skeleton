package com.cooksys.tweeter.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.service.HashtagService;
import com.cooksys.tweeter.service.TweetService;

@RestController
@RequestMapping("tags")
public class HashtagController {

	private HashtagService hashtagService;
	private TweetService tweetService;

	public HashtagController(HashtagService hashtagService, TweetService tweetService) {
		super();
		this.hashtagService = hashtagService;
		this.tweetService = tweetService;
	}

	@GetMapping
	public List<Hashtag> getHashtags(){
		return hashtagService.getHashtags();
	}
	
	@GetMapping("/{label}")
	public Set<TweetDto> findTweetsByHashtags(@RequestParam String label){
		return tweetService.findByHashtags(label);
	}
}
