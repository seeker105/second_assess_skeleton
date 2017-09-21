package com.cooksys.tweeter.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.cooksys.tweeter.dto.HashtagDto;
import com.cooksys.tweeter.entity.Hashtag;

@Mapper(componentModel="spring")
public interface HashtagMapper {
	
	Set<HashtagDto> toDtos(Set<Hashtag> hashtags);

	List<HashtagDto> toDtos(List<Hashtag> hashtags);
}
