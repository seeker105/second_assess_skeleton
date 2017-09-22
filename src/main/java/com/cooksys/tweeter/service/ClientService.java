package com.cooksys.tweeter.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.embedded.ClientData;
import com.cooksys.tweeter.embedded.Credentials;
import com.cooksys.tweeter.embedded.Profile;
import com.cooksys.tweeter.entity.Client;
import com.cooksys.tweeter.entity.Tweet;
import com.cooksys.tweeter.mapper.ClientMapper;
import com.cooksys.tweeter.mapper.TweetMapper;
import com.cooksys.tweeter.repository.ClientRepository;
import com.cooksys.tweeter.repository.TweetRepository;

@Service
public class ClientService {

	private final boolean DELETED = true;
	private final boolean NOT_DELETED = false;
	private ClientRepository clientRepository;
	private ClientMapper clientMapper;
	private TweetRepository tweetRepository;
	private TweetMapper tweetMapper;

	public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, TweetRepository tweetRepository,
			TweetMapper tweetMapper) {
		super();
		this.clientRepository = clientRepository;
		this.clientMapper = clientMapper;
		this.tweetRepository = tweetRepository;
		this.tweetMapper = tweetMapper;
	}

	public boolean userNameExists(String userName) {
		Client client = clientRepository.findByUserName(userName);
//		System.out.println("\n\n\n\n Client found by userName= " + client + "\n\n\n\n\n");
		return client != null;
	}

	@Transactional
	public ClientDto create(Credentials credentials, Profile profile) {
//		System.out.println("\n\n\n\n---\n" + credentials + "\n" + profile + "\n---\n");
		Client client = new Client(credentials, profile);
		return clientMapper.toDto(clientRepository.save(client));
	}
	
	@Transactional
	public ClientDto create(boolean deleted, Credentials credentials, Profile profile) {
		Client client = new Client(deleted, credentials, profile);
		return clientMapper.toDto(clientRepository.save(client));
	}
	
	@Transactional
	public ClientDto create(ClientData clientData){
		Client client = new Client(clientData.getCredentials(), clientData.getProfile());
		return clientMapper.toDto(clientRepository.save(client));
	}

	public Set<ClientDto> findClients() {
		return clientMapper.toDtos(clientRepository.findByDeleted(false));
	}
	
	public ClientDto findByUserName(String userName){
		return clientMapper.toDto(clientRepository.findByUserName(userName));
	}

	@Transactional
	public ClientDto activateClient(ClientDto clientDto) {
		Client client = clientRepository.findByUserName(clientDto.getUserName());
		client.setDeleted(false);
		// Activate client's tweets
		List<Tweet> clientTweets = tweetRepository.findByAuthorAndDeleted(client, DELETED);
		for (Tweet t : clientTweets){
			t.setDeleted(NOT_DELETED);
			tweetRepository.save(t);
		}
		clientRepository.saveAndFlush(client);
		return clientMapper.toDto(client);
	}
	
	public boolean clientIsDeleted(String userName){
		return findByUserName(userName).isDeleted();
	}
	
	@Transactional
	public ClientDto updateClient(ClientData clientData) {
		Client client = clientRepository.findByUserName(clientData.getUserName());
//		client.setProfile(clientData.getProfile());
		client.getProfile().setEmail(clientData.getProfile().getEmail());
		client.getProfile().setFirstName(clientData.getProfile().getFirstName());
		client.getProfile().setLastName(clientData.getProfile().getLastName());
		client.getProfile().setPhone(clientData.getProfile().getPhone());
		clientRepository.saveAndFlush(client);
		return clientMapper.toDto(client);
	}

	public boolean validatePassword(ClientData clientData) {
		Client client = clientRepository.findByUserName(clientData.getUserName());
		System.out.println("\n\n\n\n\nclient = " + client + "\n\n\n\n\n");
		return clientData.getPassword().equals(client.getCredentials().getPassword());
	}

	public boolean validatePassword(Credentials credentials) {
		Client client = clientRepository.findByUserName(credentials.getUserLogin());
		return credentials.getPassword().equals(client.getCredentials().getPassword());
	}

	@Transactional
	public ClientDto deleteClient(String userName) {
		Client client = clientRepository.findByUserName(userName);
		// Delete client's tweets
		List<Tweet> clientTweets = tweetRepository.findByAuthorAndDeleted(client, NOT_DELETED);
		for (Tweet t : clientTweets){
			t.setDeleted(DELETED);
			tweetRepository.save(t);
		}
		client.setDeleted(DELETED);
		clientRepository.saveAndFlush(client);
		return clientMapper.toDto(client);
	}

	@Transactional
	public void follow(String followed, String follower) {
		Client followedClient = clientRepository.findByUserName(followed);
		Client followerClient = clientRepository.findByUserName(follower);
		followedClient.getFollowers().add(followerClient);
	}

	public Set<ClientDto> getFollowers(String userName) {
		Client client = clientRepository.findByUserName(userName);
		return clientMapper.toDtos(clientRepository.findByFollowersAndDeleted(client, false));
	}
	
	public Set<ClientDto> getFollowing(String userName) {
		Client client = clientRepository.findByUserName(userName);
		return clientMapper.toDtos(clientRepository.findByFollowingAndDeleted(client, false));
	}

	@Transactional
	public void unFollow(String followed, String follower) {
		Client followedClient = clientRepository.findByUserName(followed);
		Client followerClient = clientRepository.findByUserName(follower);
		followedClient.getFollowers().remove(followerClient);
	}

	public List<TweetDto> getFeed(String userName) {
		Client client = clientRepository.findByUserName(userName);
		// Get client's tweets
		List<Tweet> feed = tweetRepository.findByAuthorAndDeleted(client, NOT_DELETED);
		
		// Get tweets from people the client is following
		Set<Client> isFollowing = clientRepository.findByFollowingAndDeleted(client, NOT_DELETED);
		for (Client c : isFollowing){
			feed.addAll(tweetRepository.findByAuthorAndDeleted(c, NOT_DELETED));
		}
		
		// Sort by reverse chron
		Comparator<Tweet> compareFunc = new Comparator<Tweet>(){
			public int compare(Tweet t1, Tweet t2){
				return -t1.getPosted().compareTo(t2.getPosted());
			}
		};
		Collections.sort(feed, compareFunc);
		return tweetMapper.toDtos(feed);
	}

	public List<TweetDto> getTweets(String userName) {
		Client client = clientRepository.findByUserName(userName);
		List<Tweet> tweets = tweetRepository.findByAuthorAndDeleted(client, NOT_DELETED);
		return tweetMapper.toDtos(tweets);
	}

	public List<TweetDto> getMentions(String userName) {
		Client client = clientRepository.findByUserName(userName);
		List<Tweet> tweets = tweetRepository.findByMentionedByAndDeleted(client, NOT_DELETED);
		return tweetMapper.toDtos(tweets);
	}
	
}
