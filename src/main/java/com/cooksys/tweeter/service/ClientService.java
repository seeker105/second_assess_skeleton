package com.cooksys.tweeter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.tweeter.entity.Client;
import com.cooksys.tweeter.repository.ClientRepository;

@Service
public class ClientService {

	private ClientRepository clientRepository;

	public ClientService(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	public boolean userNameExists(String userName) {
		List<Client> clients = clientRepository.findByUserName(userName);
		System.out.println(clients);
		return clients.size() > 0;
	}

	public Client create(String userName) {
		Client client = new Client(userName);
		return clientRepository.save(client);
	}
	
	
}
