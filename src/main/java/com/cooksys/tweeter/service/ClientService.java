package com.cooksys.tweeter.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.embedded.ClientData;
import com.cooksys.tweeter.embedded.Credentials;
import com.cooksys.tweeter.embedded.Profile;
import com.cooksys.tweeter.entity.Client;
import com.cooksys.tweeter.mapper.ClientMapper;
import com.cooksys.tweeter.repository.ClientRepository;

@Service
public class ClientService {

	private ClientRepository clientRepository;
	private ClientMapper clientMapper;

	public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
		super();
		this.clientRepository = clientRepository;
		this.clientMapper = clientMapper;
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
		clientRepository.saveAndFlush(client);
		return clientMapper.toDto(client);
	}
	
	public boolean clientIsDeleted(String userName){
		return findByUserName(userName).isDeleted();
	}
	
	@Transactional
	public ClientDto updateClient(ClientData clientData) {
		Client client = clientRepository.findByUserName(clientData.getUserName());
		client.setProfile(clientData.getProfile());
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
		client.setDeleted(true);
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
	
}
