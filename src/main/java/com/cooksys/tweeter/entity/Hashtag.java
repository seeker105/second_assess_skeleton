package com.cooksys.tweeter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Hashtag {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(unique=true)
	private String hashtagName;
	
	@ManyToOne
	private Tweet tweets;
	
	
	public Tweet getTweets() {
		return tweets;
	}

	public void setTweets(Tweet tweets) {
		this.tweets = tweets;
	}

	public Hashtag() {
		super();
	}

	public Hashtag(String hashtagName) {
		super();
		this.hashtagName = hashtagName;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getHashtagName() {
		return hashtagName;
	}
	
	public void setHashtagName(String hashtagName) {
		this.hashtagName = hashtagName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Hashtag)) {
			return false;
		}
		Hashtag other = (Hashtag) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Hashtag [id=" + id + ", hashtagName=" + hashtagName + "]";
	}
}
