package com.cooksys.tweeter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.HashtagDto;
import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.repository.HashtagRepository;
import com.cooksys.tweeter.service.HashtagService;
import com.cooksys.tweeter.service.TweetService;

@RestController
@RequestMapping("tags")
public class HashtagController {

	private HashtagService hashtagService;
	private HashtagRepository hashtagRepository;
	private TweetService tweetService;

	public HashtagController(HashtagService hashtagService, HashtagRepository hashtagRepository,
			TweetService tweetService) {
		super();
		this.hashtagService = hashtagService;
		this.hashtagRepository = hashtagRepository;
		this.tweetService = tweetService;
	}

	@GetMapping
	public List<HashtagDto> getHashtags(){
		return hashtagService.getHashtags();
	}
	
	@GetMapping("/{label}")
	public List<TweetDto> findTweetsByHashtags(@RequestParam String label, HttpServletResponse response){
		if (!validHashtag(label)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.findByHashtags(label);
	}
	
	private boolean validHashtag(String hashtagName){
		Hashtag tag = hashtagRepository.findByHashtagName(hashtagName);
		if (tag != null)
			return true;
		return false;
	}
}
