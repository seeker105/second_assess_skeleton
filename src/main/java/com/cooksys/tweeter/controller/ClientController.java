package com.cooksys.tweeter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
		System.out.println("\n\n\n\n\n Create Client clientData = " + clientData + "\n\n\n\n\n");
		ClientDto clientDto = clientService.findByUserName(clientData.getUserName());
		if (clientDto != null && clientService.userNameExists(clientDto.getUserName())){
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
		
//		System.out.println("\n\n\n\n\nclient.isDeleted = " + clientService.userNameExists(userName) + "\n\n\n\n\n");
		if (!clientService.userNameExists(userName) || clientService.clientIsDeleted(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.findByUserName(userName);
	}
	
	@PatchMapping("/@{userName}")
	public ClientDto updateClientProfile(@RequestBody ClientData clientData, HttpServletResponse response){
		
		System.out.println("\n\n\n\n\nvalidClient(clientData) = " + validClient(clientData) + "\n\n\n\n\n");
		if (!validClient(clientData)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.updateClient(clientData);
	}
	
	private boolean validClient(ClientData clientData){
		String userName = clientData.getUserName();
		if (!clientService.userNameExists(userName) || clientService.clientIsDeleted(userName))
			return false;
//		System.out.println("\n\n\n\n\nclientService.validatePassword(clientData) = " + clientService.validatePassword(clientData) + "\n\n\n\n\n");
		return clientService.validatePassword(clientData);
	}
	
	
}
