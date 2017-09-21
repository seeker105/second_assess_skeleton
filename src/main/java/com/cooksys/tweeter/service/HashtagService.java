package com.cooksys.tweeter.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.tweeter.dto.HashtagDto;
import com.cooksys.tweeter.entity.Hashtag;
import com.cooksys.tweeter.mapper.HashtagMapper;
import com.cooksys.tweeter.repository.HashtagRepository;

@Service
public class HashtagService {

	HashtagRepository hashtagRepository;
	HashtagMapper hashtagMapper;

	public HashtagService(HashtagRepository hashtagRepository, HashtagMapper hashtagMapper) {
		super();
		this.hashtagRepository = hashtagRepository;
		this.hashtagMapper = hashtagMapper;
	}

	@Transactional
	public Hashtag create(String hashtagName){
		Hashtag tag = new Hashtag(hashtagName);
		return hashtagRepository.save(tag);
	}
	
	public boolean tagExists(String label) {
		Hashtag hashtag = hashtagRepository.findByHashtagName(label);
		return hashtag != null;
	}

	public List<HashtagDto> getHashtags() {
		return hashtagMapper.toDtos(hashtagRepository.findAll());
	}


	
}
