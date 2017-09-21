package com.cooksys.tweeter.service;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.tweeter.controller.ClientController;
import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.embedded.SimpleTweetData;
import com.cooksys.tweeter.entity.Client;
import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.entity.Tweet;
import com.cooksys.tweeter.mapper.TweetMapper;
import com.cooksys.tweeter.repository.ClientRepository;
import com.cooksys.tweeter.repository.HashtagRepository;
import com.cooksys.tweeter.repository.TweetRepository;

@Service
public class TweetService {
	

	private HashtagService hashtagService;
	private ClientController clientController;
	private ClientService clientService;
	private ClientRepository clientRepository;
	private TweetRepository tweetRepository;
	private HashtagRepository hashtagRepository;
	private TweetMapper tweetMapper;
	
	public TweetService(HashtagService hashtagService, ClientController clientController, ClientService clientService,
			ClientRepository clientRepository, TweetRepository tweetRepository, HashtagRepository hashtagRepository,
			TweetMapper tweetMapper) {
		super();
		this.hashtagService = hashtagService;
		this.clientController = clientController;
		this.clientService = clientService;
		this.clientRepository = clientRepository;
		this.tweetRepository = tweetRepository;
		this.hashtagRepository = hashtagRepository;
		this.tweetMapper = tweetMapper;
	}
	
	@Transactional
	public TweetDto createSimpleTweet(SimpleTweetData simpleTweetData){
		String hashtagName, userName;
		Hashtag tag;
		Client client, author;
		String content = simpleTweetData.getContent();
		Tweet tweet = new Tweet();
		
		// Set author
		author = clientRepository.findByUserName(simpleTweetData.getCredentials().getUserLogin());
		author.getTweets().add(tweet);
		
		// Set timestamp
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		
		// Set content
		tweet.setContent(content);
		
		// Set deleted
		tweet.setDeleted(false);
		
		// Process hashtags
		Pattern p = Pattern.compile("#\\w*");
		Matcher m = p.matcher(content);
		while(m.find()){
			hashtagName = m.group().substring(1);
			System.out.println(hashtagName);
			if (!hashtagService.tagExists(hashtagName)){
				tag = hashtagService.create(hashtagName);
			} else {
				tag = hashtagRepository.findByHashtagName(hashtagName);
			}
			tweet.getHashtags().add(tag);
		}
		
		// Process mentions
		p = Pattern.compile("@\\w");
		m = p.matcher(content);
		while(m.find()){
			userName = m.group().substring(1);
			System.out.println(userName);
			if (clientController.validClient(userName)){
				client = clientRepository.findByUserName(userName);
				tweet.getMentions().add(client);
			}
		}
		return tweetMapper.toDto(tweetRepository.save(tweet));
	}
	

}
