package com.cooksys.tweeter.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Tweet {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	private Client author;
	
	private Timestamp posted;
	
	private String content;
	
	@ManyToOne
	private Tweet inReplyTo;
	
	@OneToMany(mappedBy="inRepyTo")
	private ArrayList<Tweet> replies;
	
	@ManyToOne
	private Tweet reposts;
	
	@OneToMany(mappedBy="repostOf")
	private ArrayList<Tweet> repostOf;
	
	private boolean deleted;
	
	@OneToMany(mappedBy="tweets")
	private Set<Hashtag> hashtags;
	
	@ManyToMany(mappedBy="mentions")
	private Set<Client> mentions;

	public Tweet() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Client getAuthor() {
		return author;
	}

	public void setAuthor(Client author) {
		this.author = author;
	}

	public Timestamp getPosted() {
		return posted;
	}

	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Tweet getInReplyTo() {
		return inReplyTo;
	}

	public void setInReplyTo(Tweet inReplyTo) {
		this.inReplyTo = inReplyTo;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Set<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public Set<Client> getMentions() {
		return mentions;
	}

	public void setMentions(Set<Client> mentions) {
		this.mentions = mentions;
	}
	
	public ArrayList<Tweet> getReplies() {
		return replies;
	}

	public void setReplies(ArrayList<Tweet> replies) {
		this.replies = replies;
	}

	public Tweet getReposts() {
		return reposts;
	}

	public void setReposts(Tweet reposts) {
		this.reposts = reposts;
	}

	public ArrayList<Tweet> getRepostOf() {
		return repostOf;
	}

	public void setRepostOf(ArrayList<Tweet> repostOf) {
		this.repostOf = repostOf;
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
		if (!(obj instanceof Tweet)) {
			return false;
		}
		Tweet other = (Tweet) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}


	
}
