package com.cooksys.tweeter.controller;


import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.dto.HashtagDto;
import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.embedded.Context;
import com.cooksys.tweeter.embedded.Credentials;
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
	public List<TweetDto> getTweets(){
		return tweetService.getTweets();
	}
	
	@PostMapping
	public TweetDto postTweet(@RequestBody SimpleTweetData simpleTweetData, HttpServletResponse response){
		if (simpleTweetData.getContent() == null || simpleTweetData.getContent().equals("")){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
				
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
	
	
	@PostMapping("/{id}/like")
	public void like(@RequestParam Integer id, @RequestBody Credentials credentials, HttpServletResponse response){
		if (!clientController.validClient(credentials) || !tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		tweetService.like(id, credentials.getUserLogin());
	}
	
	@PostMapping("/{id}/reply")
	public TweetDto replyTo(@RequestParam Integer id, @RequestBody SimpleTweetData tweetData, HttpServletResponse response){
		if (!clientController.validClient(tweetData.getCredentials()) || !tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		if (tweetData.getContent() == null || tweetData.getContent().equals("")){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.replyTo(id, tweetData);
	}
	
	@PostMapping("/{id}/repost")
	public TweetDto repost(@RequestParam Integer id, @RequestBody Credentials credentials, HttpServletResponse response){
		if (!clientController.validClient(credentials) || !tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.repost(id, credentials.getUserLogin());
	}
	
	@GetMapping("{id}/tags")
	public Set<HashtagDto> getTags(@RequestParam Integer id, HttpServletResponse response){
		if (!tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		System.out.println("\n\n\n\n\n\n\n output" + tweetService.getTagsByTweet(id) + "\n\n\n\n\n");
		return tweetService.getTagsByTweet(id);
	}
	
	@GetMapping("/{id}/likes")
	public Set<ClientDto> getLikes(@RequestParam Integer id, HttpServletResponse response){
		if (!tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
//		System.out.println("\n\n\n\n\n\n\n output" + tweetService.) + "\n\n\n\n\n");
		return tweetService.getLikesByTweet(id);
	}
	
	@GetMapping("/{id}/context")
	public Context getContext(@RequestParam Integer id, HttpServletResponse response){
		if (!tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.getContext(id);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetDto> getReplies(@RequestParam Integer id, HttpServletResponse response){
		if (!tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
//		System.out.println("\n\n\n\n\n\n\n output" + tweetService.) + "\n\n\n\n\n");
		return tweetService.getRepliesByTweet(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetDto> getReposts(@RequestParam Integer id, HttpServletResponse response){
		if (!tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
//		System.out.println("\n\n\n\n\n\n\n output" + tweetService.) + "\n\n\n\n\n");
		return tweetService.getRepostsByTweet(id);
	}
	
	@GetMapping("/{id}/mentions")
	public List<ClientDto> getMentions(@RequestParam Integer id, HttpServletResponse response){
		if (!tweetService.tweetExists(id)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
//		System.out.println("\n\n\n\n\n\n\n output" + tweetService.) + "\n\n\n\n\n");
		return tweetService.getMentionsByTweet(id);
	}
	

	
}
