package com.cooksys.tweeter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.tweeter.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {

	Client findByUserName(String userName);

	List<Client> findByDeleted(boolean deleted);

}
