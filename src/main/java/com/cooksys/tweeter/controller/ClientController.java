package com.cooksys.tweeter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.embedded.ClientData;
import com.cooksys.tweeter.entity.Client;
import com.cooksys.tweeter.service.ClientService;

@RestController
@RequestMapping("users")
public class ClientController {

	private ClientService clientService;

	public ClientController(ClientService clientService) {
		super();
		this.clientService = clientService;
	}
	
	@GetMapping
	public List<ClientDto> getClients(){
		return clientService.findClients();
	}
	
	@PostMapping
	public ClientDto createClient(@RequestBody ClientData clientData, HttpServletResponse response){
		System.out.println("\n\n\n\n\nclientData = " + clientData + "\n\n\n\n\n");
		Client client = clientService.findByUserName(clientData.getUserName());
		if (clientService.userNameExists(client.getUserName())){
			if (client.isDeleted()){
				return clientService.activateClient(client);
			}
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return null;
		}
		return clientService.create(clientData);
	}
	
	
}
