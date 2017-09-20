package com.cooksys.tweeter.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
	
	Set<Client> findByDeleted(boolean deleted);

	Client findByUserName(String userName);
	
	Set<Client> findByFollowersAndDeleted(Client client, boolean deleted);

	Set<Client> findByFollowingAndDeleted(Client client, boolean deleted);

	Client findByIdAndFollowing(Integer id, Client followedClient);

//	Client findInFollowers(Client followerClient);

}
