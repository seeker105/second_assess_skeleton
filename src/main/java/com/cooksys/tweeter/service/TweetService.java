package com.cooksys.tweeter.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.tweeter.controller.ClientController;
import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.dto.HashtagDto;
import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.embedded.Context;
import com.cooksys.tweeter.embedded.SimpleTweetData;
import com.cooksys.tweeter.entity.Client;
import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.entity.Tweet;
import com.cooksys.tweeter.mapper.ClientMapper;
import com.cooksys.tweeter.mapper.HashtagMapper;
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
	private HashtagMapper hashtagMapper;
	private ClientMapper clientMapper;
	
	public TweetService(HashtagService hashtagService, ClientController clientController, ClientService clientService,
			ClientRepository clientRepository, TweetRepository tweetRepository, HashtagRepository hashtagRepository,
			TweetMapper tweetMapper, HashtagMapper hashtagMapper, ClientMapper clientMapper) {
		super();
		this.hashtagService = hashtagService;
		this.clientController = clientController;
		this.clientService = clientService;
		this.clientRepository = clientRepository;
		this.tweetRepository = tweetRepository;
		this.hashtagRepository = hashtagRepository;
		this.tweetMapper = tweetMapper;
		this.hashtagMapper = hashtagMapper;
		this.clientMapper = clientMapper;
	}

	@Transactional
	public TweetDto createSimpleTweet(SimpleTweetData simpleTweetData){
		Client author= clientRepository.findByUserName(simpleTweetData.getCredentials().getUserLogin());
		String content = simpleTweetData.getContent();
		Tweet tweet = new Tweet(NOT_DELETED);
		tweetRepository.saveAndFlush(tweet);
		System.out.println("\n\n\n\n\n\n tweet id = " + tweet.getId() + "\n\n\n\n\n\n");
		
		// Set timestamp
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		
		// Set content
		tweet.setContent(content);
		
		// Process hashtags
		processHashtags(tweet, content);
		
		// Process mentions
		processMentions(tweet, content);

		// Set author
		tweet.setAuthor(author);
//		author.getTweets().add(tweet);
		return tweetMapper.toDto(tweetRepository.save(tweet));
	}

	public Set<TweetDto> findByHashtags(String hashtagName) {
		return tweetMapper.toDtos(tweetRepository.findByHashtagsOrderByPosted(hashtagName));
	}

	public List<TweetDto> getTweets() {
		List<Tweet> allTweets = tweetRepository.findAll(new Sort(Sort.Direction.DESC, "posted"));
		List<Tweet> returnedTweets = new ArrayList<Tweet>();
		
		for (Tweet t : allTweets){
			if (!t.isDeleted())
				returnedTweets.add(t);
		}
		return tweetMapper.toDtos(returnedTweets);
		
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
		Tweet tweet = new Tweet(NOT_DELETED);
		tweetRepository.saveAndFlush(tweet);
		Tweet inReplyTo = tweetRepository.findById(id);
		
		
		// Set timestamp
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		
		// Set content
		tweet.setContent(content);
		
		// Process hashtags
		tweet = processHashtags(tweet, content);
		
		// Process mentions
		tweet = processMentions(tweet, content);
		
		// Set inReplyTo
		tweet.setInReplyTo(inReplyTo);

		// Set author
		tweet.setAuthor(author);
//		author.getTweets().add(tweet);
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
		Pattern p = Pattern.compile("@\\w*");
		Matcher m = p.matcher(content);
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
		while(m.find()){
			userName = m.group().substring(1);
			System.out.println("Mention name = " + userName);
			System.out.println("Valid client = " + clientController.validClient(userName));
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
		Tweet tweet = new Tweet(NOT_DELETED);
		tweetRepository.saveAndFlush(tweet);
		Tweet repostOf = tweetRepository.findById(id);
		
		// Set timestamp
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		
		// Set repostOf
		tweet.setRepostOf(repostOf);
		
		// Set Content
		tweet.setContent(repostOf.getContent());

		// Set author
		tweet.setAuthor(author);
//		author.getTweets().add(tweet);
		return tweetMapper.toDto(tweetRepository.save(tweet));
	}

	public Set<HashtagDto> getTagsByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		System.out.println("\n\n\n\n\n\n\n output in service" + tweet.getHashtags() + "\n\n\n\n\n");
		return hashtagMapper.toDtos(tweet.getHashtags());
	}

	public Set<ClientDto> getLikesByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
//		System.out.println("\n\n\n\n\n\n\n output in service" + tweet.getHashtags() + "\n\n\n\n\n");
		return clientMapper.toDtos(clientRepository.findByLikesAndDeleted(tweet, NOT_DELETED));
	}

	public List<TweetDto> getRepliesByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
//		System.out.println("\n\n\n\n\n\n\n output in service" + tweet.getHashtags() + "\n\n\n\n\n");
		return tweetMapper.toDtos(tweetRepository.findByinReplyToAndDeleted(tweet, NOT_DELETED));
}

	public List<ClientDto> getMentionsByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
//		System.out.println("\n\n\n\n\n\n\n output in service" + tweet.getHashtags() + "\n\n\n\n\n");
		return clientMapper.toDtos(clientRepository.findByMentionsAndDeleted(tweet, NOT_DELETED));
	}

	public List<TweetDto> getRepostsByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		List<Tweet> tweets = tweetRepository.findByRepostOfAndDeleted(tweet, NOT_DELETED);
		return tweetMapper.toDtos(tweets);
	}

	public Context getContext(Integer id) {
		Tweet found;
		Tweet target = tweetRepository.findById(id);
		Tweet tweet = target;
		Context context = new Context(tweetMapper.toDto(target));
		
		// Find the Before chain
		ArrayList<Tweet> before = new ArrayList<Tweet>();
		while (tweet.getInReplyTo() != null){
			found = tweet.getInReplyTo();
			if (!found.isDeleted())
				before.add(found);
			tweet = found;
		}
		// Sort by reverse chron
		Comparator<Tweet> compareFunc = new Comparator<Tweet>(){
			public int compare(Tweet t1, Tweet t2){
				return -t1.getPosted().compareTo(t2.getPosted());
			}
		};
		Collections.sort(before, compareFunc);
		context.setBefore(tweetMapper.toDtos(before));
		
		// Traverse the After tree
		ArrayList<Tweet> after = new ArrayList<Tweet>();
		traverseAfterTree(target, after);
		
		// Sort by reverse chron
		Collections.sort(after, compareFunc);
		context.setAfter(tweetMapper.toDtos(after));
		
		return context;
	}

	private void traverseAfterTree(Tweet current, ArrayList<Tweet> after) {
		List<Tweet> replies = current.getReplies();
		for (Tweet tweet : replies){
			if (!tweet.isDeleted())
				after.add(tweet);
			traverseAfterTree(tweet, after);
		}
	}
	
	

}
