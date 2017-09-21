package com.cooksys.tweeter.entity;

import java.sql.Timestamp;
import java.util.List;
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
	
	private boolean deleted;
	
	@OneToMany(mappedBy="tweets")
	private Set<Hashtag> hashtags;
	
	@ManyToMany(mappedBy="mentions")
	private Set<Client> mentionedBy;
	
	@ManyToMany(mappedBy="likes")
	private Set<Client> likedBy;

	@ManyToOne
	private Tweet inReplyTo;
	
	@OneToMany
	private List<Tweet> replies;
	
	@ManyToOne
	private Tweet repostOf;
	
	@OneToMany(mappedBy="repostOf")
	private List<Tweet> reposts;
	
	public List<Tweet> getReplies() {
		return replies;
	}

	public void setReplies(List<Tweet> replies) {
		this.replies = replies;
	}

	public Tweet getRepostOf() {
		return repostOf;
	}

	public void setRepostOf(Tweet repostOf) {
		this.repostOf = repostOf;
	}

	public List<Tweet> getReposts() {
		return reposts;
	}

	public void setReposts(List<Tweet> reposts) {
		this.reposts = reposts;
	}
	
	public Tweet getInReplyTo() {
		return inReplyTo;
	}
	
	public void setInReplyTo(Tweet inReplyTo) {
		this.inReplyTo = inReplyTo;
	}

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

	public Set<Client> getMentionedBy() {
		return mentionedBy;
	}

	public void setMentionedBy(Set<Client> mentions) {
		this.mentionedBy = mentions;
	}

	public Set<Client> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(Set<Client> likedBy) {
		this.likedBy = likedBy;
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
