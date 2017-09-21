package com.cooksys.tweeter.embedded;

import java.util.ArrayList;
import java.util.List;

import com.cooksys.tweeter.dto.TweetDto;

public class Context {
	
	private TweetDto target;
	private List<TweetDto> before;
	private List<TweetDto> after;
	
	public Context(TweetDto target) {
		super();
		this.target = target;
		this.before = new ArrayList<TweetDto>();
		this.after = new ArrayList<TweetDto>();
	}

	public TweetDto getTarget() {
		return target;
	}

	public void setTarget(TweetDto target) {
		this.target = target;
	}

	public List<TweetDto> getBefore() {
		return before;
	}

	public void setBefore(List<TweetDto> before) {
		this.before = before;
	}

	public List<TweetDto> getAfter() {
		return after;
	}

	public void setAfter(List<TweetDto> after) {
		this.after = after;
	}
}
