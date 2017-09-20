package com.cooksys.tweeter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.embedded.ClientData;
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
		ClientDto clientDto = clientService.findByUserName(clientData.getUserName());
		if (clientService.userNameExists(clientDto.getUserName())){
			if (clientDto.isDeleted()){
				return clientService.activateClient(clientDto);
			}
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return null;
		}
		return clientService.create(clientData);
	}
	
	@GetMapping("/@{userName}")
	public ClientDto findByUserName(@RequestParam String userName, HttpServletResponse response){
		
		System.out.println("\n\n\n\n\nclient.isDeleted = " + clientService.userNameExists(userName) + "\n\n\n\n\n");
		if (!clientService.userNameExists(userName) || clientService.clientIsDeleted(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.findByUserName(userName);
	}
	
	
}
