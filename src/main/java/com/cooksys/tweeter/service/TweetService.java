package com.cooksys.tweeter.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
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
	
	private final boolean NOT_DELETED = false;
	private final boolean DELETED = true;
	

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
		Client author= clientRepository.findByUserName(simpleTweetData.getCredentials().getUserLogin());
		String content = simpleTweetData.getContent();
		Tweet tweet = new Tweet();
		
		// Set timestamp
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		
		// Set content
		tweet.setContent(content);
		
		// Set deleted
		tweet.setDeleted(NOT_DELETED);
		
		// Process hashtags
		tweet = processHashtags(tweet, content);
		
		// Process mentions
		tweet = processMentions(tweet, content);

		// Set author
		author.getTweets().add(tweet);
		return tweetMapper.toDto(tweetRepository.save(tweet));
	}

	public Set<TweetDto> findByHashtags(String hashtagName) {
		return tweetMapper.toDtos(tweetRepository.findByHashtagsOrderByPosted(hashtagName));
	}

	public List<TweetDto> getTweets() {
		return tweetMapper.toDtos(tweetRepository.findAll(new Sort(Sort.Direction.DESC, "posted")));
	}

	public TweetDto getTweetById(Integer id) {
		return tweetMapper.toDto(tweetRepository.findByIdAndDeleted(id, NOT_DELETED));
	}

	@Transactional
	public TweetDto deleteTweetById(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		if (tweet == null || tweet.isDeleted())
			return null;
		tweet.setDeleted(DELETED);
		return tweetMapper.toDto(tweet);
	}

	public boolean tweetExists(Integer id){
		return getTweetById(id) != null;
	}

	@Transactional
	public void like(Integer id, String userName) {
		Tweet tweet = tweetRepository.findById(id);
		Client client = clientRepository.findByUserName(userName);
		client.getLikes().add(tweet);
	}

	@Transactional
	public TweetDto replyTo(Integer id, SimpleTweetData tweetData) {
		Client author= clientRepository.findByUserName(tweetData.getCredentials().getUserLogin());
		String content = tweetData.getContent();
		Tweet tweet = new Tweet();
		Tweet inReplyTo = tweetRepository.findById(id);
		
		
		// Set timestamp
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		
		// Set content
		tweet.setContent(content);
		
		// Set deleted
		tweet.setDeleted(NOT_DELETED);
		
		// Process hashtags
		tweet = processHashtags(tweet, content);
		
		// Process mentions
		tweet = processMentions(tweet, content);
		
		// Set inReplyTo
		tweet.setInReplyTo(inReplyTo);

		// Set author
		author.getTweets().add(tweet);
		return tweetMapper.toDto(tweetRepository.save(tweet));
	}
	
	private Tweet processHashtags(Tweet tweet, String content){
		String hashtagName;
		Hashtag tag;
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
		return tweet;
	}

	private Tweet processMentions(Tweet tweet, String content){
		String userName;
		Client client;
		Pattern p = Pattern.compile("@\\w");
		Matcher m = p.matcher(content);
		while(m.find()){
			userName = m.group().substring(1);
			System.out.println(userName);
			if (clientController.validClient(userName)){
				client = clientRepository.findByUserName(userName);
				tweet.getMentionedBy().add(client);
			}
		}
		return tweet;
	}

	@Transactional
	public TweetDto repost(Integer id, String userName) {
		Client author = clientRepository.findByUserName(userName);
		Tweet tweet = new Tweet();
		Tweet repostOf = tweetRepository.findById(id);
		
		// Set timestamp
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		
		// Set deleted
		tweet.setDeleted(NOT_DELETED);
		
		// Set repostOf
		tweet.setRepostOf(repostOf);

		// Set author
		author.getTweets().add(tweet);
		return tweetMapper.toDto(tweetRepository.save(tweet));
	}
	
	

}
