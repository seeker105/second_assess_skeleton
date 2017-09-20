package com.cooksys.tweeter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
	
	List<Client> findByDeleted(boolean deleted);

	Client findByUserName(String userName);
	
	List<Client> findByFollowersAndDeleted(Client client, boolean deleted);

	List<Client> findByFollowingAndDeleted(Client client, boolean deleted);

}
