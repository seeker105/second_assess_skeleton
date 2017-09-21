package com.cooksys.tweeter.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import com.cooksys.tweeter.dto.TweetDto;
import com.cooksys.tweeter.entity.Tweet;

@Mapper(componentModel="spring")
public interface TweetMapper {

	TweetDto toDto(Tweet tweet);
	
	Tweet fromDto(TweetDto tweetDto);
	
	Set<TweetDto> toDtos(Set<Tweet> set);

}
