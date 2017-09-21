package com.cooksys.tweeter.controller;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.embedded.SimpleTweetData;
import com.cooksys.tweeter.mapper.TweetMapper;
import com.cooksys.tweeter.repository.ClientRepository;
import com.cooksys.tweeter.repository.HashtagRepository;
import com.cooksys.tweeter.repository.TweetRepository;
import com.cooksys.tweeter.service.ClientService;
import com.cooksys.tweeter.service.HashtagService;
import com.cooksys.tweeter.service.TweetService;

@RestController
@RequestMapping("tweets")
public class TweetController {

	private TweetService tweetService;
	private HashtagService hashtagService;
	private ClientController clientController;
	private ClientService clientService;
	private ClientRepository clientRepository;
	private TweetRepository tweetRepository;
	private HashtagRepository hashtagRepository;
	private TweetMapper tweetMapper;

	public TweetController(TweetService tweetService, HashtagService hashtagService, ClientController clientController,
			ClientService clientService, ClientRepository clientRepository, TweetRepository tweetRepository,
			HashtagRepository hashtagRepository, TweetMapper tweetMapper) {
		super();
		this.tweetService = tweetService;
		this.hashtagService = hashtagService;
		this.clientController = clientController;
		this.clientService = clientService;
		this.clientRepository = clientRepository;
		this.tweetRepository = tweetRepository;
		this.hashtagRepository = hashtagRepository;
		this.tweetMapper = tweetMapper;
	}

	@GetMapping
	public Set<TweetDto> getTweets(){
		return tweetService.getTweets();
	}
	
	@PostMapping
	public TweetDto postTweet(@RequestBody SimpleTweetData simpleTweetData, HttpServletResponse response){
		return tweetService.createSimpleTweet(simpleTweetData);
	}
	
	@GetMapping("/{id}")
	public TweetDto getTweetById(@RequestParam Integer id, HttpServletResponse response){
		TweetDto tweetDto = tweetService.getTweetById(id);
		if (tweetDto == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetDto;
	}
	
	@DeleteMapping("/{id}")
	public TweetDto deleteTweetById(@RequestParam Integer id, HttpServletResponse response){
		TweetDto tweetDto = tweetService.deleteTweetById(id);
		if (tweetDto == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetDto;
	}
	
	
}
