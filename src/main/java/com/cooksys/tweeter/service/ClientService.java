package com.cooksys.tweeter.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
		System.out.println("\n\n\n\n Client found by userName= " + client + "\n\n\n\n\n");
		return client != null;
	}

	public ClientDto create(Credentials credentials, Profile profile) {
		System.out.println("\n\n\n\n---\n" + credentials + "\n" + profile + "\n---\n");

		Client client = new Client(credentials, profile);
		return clientMapper.toDto(clientRepository.save(client));
	}
	
	public ClientDto create(boolean deleted, Credentials credentials, Profile profile) {
		Client client = new Client(deleted, credentials, profile);
		return clientMapper.toDto(clientRepository.save(client));
	}
	
	public ClientDto create(ClientData clientData){
		Client client = new Client(clientData.getCredentials(), clientData.getProfile());
		return clientMapper.toDto(clientRepository.save(client));
	}

	public List<ClientDto> findClients() {
		return clientMapper.toDtos(clientRepository.findByDeleted(false));
	}
	
//	public boolean clientDeleted(ClientData clientData){
//		
//	}
	
	
}
