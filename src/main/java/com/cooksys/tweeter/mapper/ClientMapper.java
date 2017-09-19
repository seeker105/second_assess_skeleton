package com.cooksys.tweeter.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.tweeter.dto.ClientDto;
import com.cooksys.tweeter.entity.Client;

@Mapper(componentModel="spring")
public interface ClientMapper {

	ClientDto toDto(Client client);
	
	Client fromDto(ClientDto clientDto);
	
	List<ClientDto> toDtos(List<Client> clients);
}
