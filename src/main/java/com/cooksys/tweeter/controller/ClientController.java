package com.cooksys.tweeter.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.embedded.ClientData;
import com.cooksys.tweeter.embedded.Credentials;
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
	public Set<ClientDto> getClients(){
		return clientService.findClients();
	}
	
	@PostMapping
	public ClientDto createClient(@RequestBody ClientData clientData, HttpServletResponse response){
		if (!validClientData(clientData)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
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
		if (!validClientData(clientData)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		System.out.println("\n\n\n\n\nvalidClient(clientData) = " + validClient(clientData) + "\n\n\n\n\n");
		if (!validClient(clientData)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.updateClient(clientData);
	}
	
	@DeleteMapping("/@{userName}")
	public ClientDto deleteClient(@RequestBody Credentials credentials, HttpServletResponse response){
		if (!validClient(credentials)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.deleteClient(credentials.getUserLogin());
	}
	
	@PostMapping("/@{userName}/follow")
	public void followClient(@RequestParam String userName, @RequestBody Credentials followerCred, HttpServletResponse response){
		if (!validClient(followerCred) || !validClient(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		clientService.follow(followerCred.getUserLogin(), userName);
	}
	
	@PostMapping("@{username}/unfollow")
	public void unFollowClient(@RequestParam String username, @RequestBody Credentials followerCred, HttpServletResponse response){
		if (!validClient(followerCred) || !validClient(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		clientService.unFollow(followerCred.getUserLogin(), username);
	}
	
	@GetMapping("/@{username}/feed")
	public List<TweetDto> getFeed(@RequestParam String username, HttpServletResponse response){
		if (!clientService.userNameExists(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.getFeed(username);
	}
	
	@GetMapping("/@{username}/tweets")
	public List<TweetDto> getTweets(@RequestParam String username, HttpServletResponse response){
		if (!clientService.userNameExists(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.getTweets(username);
	}
	
	@GetMapping("/@{username}/mentions")
	public List<TweetDto> getMentions(@RequestParam String username, HttpServletResponse response){
		if (!clientService.userNameExists(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.getMentions(username);
	}
	
	@GetMapping("/@{userName}/followers")
	public Set<ClientDto> getFollowers(@RequestParam String userName, HttpServletResponse response){
		if (!clientService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.getFollowers(userName);
	}
	
	@GetMapping("/@{userName}/following")
	public Set<ClientDto> getFollowing(@RequestParam String userName, HttpServletResponse response){
		if (!clientService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return clientService.getFollowing(userName);
	}

	
	private boolean validClient(ClientData clientData){
		String userName = clientData.getUserName();
		if (!clientService.userNameExists(userName) || clientService.clientIsDeleted(userName))
			return false;
		return clientService.validatePassword(clientData);
	}
	
	public boolean validClient(Credentials credentials){
		String userName = credentials.getUserLogin();
		if (!clientService.userNameExists(userName) || clientService.clientIsDeleted(userName))
			return false;
		return clientService.validatePassword(credentials);
	}
	
	public boolean validClient(String userName){
		if (!clientService.userNameExists(userName) || clientService.clientIsDeleted(userName))
			return false;
		return true;
	}
	
	private boolean validClientData(ClientData clientData){
		if (clientData.getProfile().getEmail() == null || clientData.getProfile().getEmail().equals(""))
			return false;
		if (!validCredentials(clientData.getCredentials()))
			return false;
		return true;
	}
	
	private boolean validCredentials(Credentials credentials){
		if (credentials.getUserLogin() == null || credentials.getUserLogin().equals(""))
			return false;
		if (credentials.getPassword() == null || credentials.getPassword().equals(""))
			return false;
		return true;
	}
	
	
}
