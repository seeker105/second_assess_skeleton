package com.cooksys.tweeter.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.tweeter.embedded.Credentials;
import com.cooksys.tweeter.embedded.Profile;
import com.cooksys.tweeter.embedded.SimpleTweetData;
import com.cooksys.tweeter.service.ClientService;
import com.cooksys.tweeter.service.HashtagService;
import com.cooksys.tweeter.service.TweetService;

@RestController
@RequestMapping("validate")
public class ValidateController {
	
	private HashtagService hashtagService;
	private ClientService clientService;
	private TweetService tweetService;
	
	private final boolean INACTIVE = true;
		
	public ValidateController(HashtagService hashtagService, ClientService clientService, TweetService tweetService) {
		super();
		this.hashtagService = hashtagService;
		this.clientService = clientService;
		this.tweetService = tweetService;
	}

	@PostMapping("/setup")
	public String setupData(HttpServletResponse response){
		String completeMessage = "";
		hashtagService.create("thunder");
		hashtagService.create("storm");
		hashtagService.create("window");
		hashtagService.create("door");
		completeMessage += "Tag setup completed\n";
		
		Credentials c1, c2, c3, c4, c5, c6;
		Profile p1, p2, p3, p4, p5, p6;
		
		c1 = new Credentials();
		c2 = new Credentials();
		c3 = new Credentials();
		c4 = new Credentials();
		c5 = new Credentials();
		c6 = new Credentials();
		c1.setUserLogin("mboren"); 	
		c1.setPassword("pass");
		c2.setUserLogin("csoden"); 	
		c2.setPassword("pass");
		c3.setUserLogin("ghill"); 	
		c3.setPassword("pass");
		c4.setUserLogin("tpettrey");	
		c4.setPassword("pass");
		c5.setUserLogin("nhudson"); 	
		c5.setPassword("pass");
		c6.setUserLogin("cshivers"); 
		c6.setPassword("pass");
		
		p1 = new Profile();
		p2 = new Profile();
		p3 = new Profile();
		p4 = new Profile();
		p5 = new Profile();
		p6 = new Profile();
		
		p1.setEmail("mboren@gmail.com"); 	
		p1.setFirstName("Michael");
		p1.setLastName("Boren"); 			
		p1.setPhone("1234567890");
		p2.setEmail("csoden@gmail.com"); 	
		p2.setFirstName("Christopher");
		p2.setLastName("Soden"); 			
		p2.setPhone("8529631740");
		p3.setEmail("ghill@gmail.com"); 	
		p3.setFirstName("Greg");
		p3.setLastName("Hill"); 			
		p3.setPhone("7418529630");
		p4.setEmail("tpettrey@gmail.com"); 	
		p4.setFirstName("Travis");
		p4.setLastName("Pettrey"); 			
		p4.setPhone("6894571230");
		p5.setEmail("nhudson@gmail.com"); 	
		p5.setFirstName("Nick");
		p5.setLastName("Hudson"); 			
		p5.setPhone("2135468790");
		p6.setEmail("cshivers@gmail.com"); 	
		p6.setFirstName("Chris");
		p6.setLastName("Shivers"); 			
		p6.setPhone("4561237890");
		
		
		clientService.create(c1, p1);
		clientService.create(c2, p2);
		clientService.create(c3, p3);
		clientService.create(INACTIVE, c4, p4);
		clientService.create(INACTIVE, c5, p5);
		clientService.create(INACTIVE, c6, p6);
		completeMessage += "Client setup completed\n";
		
		SimpleTweetData t1, t2, t3, t4, t5, t6, t7, t8, t9, t10; 
		
//		tweetService.createSimpleTweet(simpleTweetData)
		
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
		System.out.println("\n\n\n\n userName found by userName= " + userName + "\n\n\n\n\n");

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
