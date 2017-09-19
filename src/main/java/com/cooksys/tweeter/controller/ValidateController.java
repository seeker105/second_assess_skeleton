package com.cooksys.tweeter.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.service.ClientService;
import com.cooksys.tweeter.service.HashtagService;

@RestController
@RequestMapping("validate")
public class ValidateController {
	
	private HashtagService hashtagService;
	private ClientService clientService;
	
	public ValidateController(HashtagService hashtagService, ClientService clientService){
		this.hashtagService = hashtagService;
		this.clientService = clientService;
	}
	
	@PostMapping("/setup")
	public String setupData(HttpServletResponse response){
		String completeMessage = "";
		hashtagService.create("thunder");
		hashtagService.create("storm");
		hashtagService.create("window");
		hashtagService.create("door");
		completeMessage += "Tag setup completed\n";
		
		clientService.create("mboren");
		clientService.create("cshivers");
		clientService.create("ghill");
		clientService.create("csoden");
		clientService.create("tpettrey");
		clientService.create("nhudson");
		completeMessage += "Client setup completed\n";
		
		return completeMessage;
	}
	
	@GetMapping("/tag/exists/{label}")
	public boolean tagExists(@RequestParam String label, HttpServletResponse response){
		if (hashtagService.tagExists(label)){
			response.setStatus(HttpServletResponse.SC_FOUND);
			return true;
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return false;
	}
	
	@GetMapping("/username/exists/@{userName}")
	public boolean userNameExists(@RequestParam String userName, HttpServletResponse response){
		if (clientService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_FOUND);
			return true;
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return false;
	}
	
	@GetMapping("/username/available/@{userName}")
	public boolean userNameAvailable(@RequestParam String userName, HttpServletResponse response){
		if (!clientService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return true;
		}
		response.setStatus(HttpServletResponse.SC_FOUND);
		return false;
	}
	

}
