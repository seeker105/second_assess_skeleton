package com.cooksys.tweeter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.embedded.Credentials;
import com.cooksys.tweeter.embedded.Profile;
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
	public ClientDto createClient(Credentials credentials, Profile profile, HttpServletResponse response){
		if (clientService.userNameExists(credentials.getUserLogin())){
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return null;
		}
		clientService.create(credentials, profile);
		return null;
	}
	
	
}
