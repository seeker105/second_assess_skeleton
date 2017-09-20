package com.cooksys.tweeter.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.cooksys.tweeter.embedded.Credentials;
import com.cooksys.tweeter.embedded.Profile;

@Entity
public class Client {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String userName;
	
	private boolean deleted;
	
	private Timestamp joined;

	@Embedded
	private Profile profile;

	@Embedded
	private Credentials credentials;
	
	@ManyToMany
	private List<Client> followers;
	
	@ManyToMany(mappedBy="followers")
//	@JoinTable(name="follower_following", joinColumns=@JoinColumn(name="following_id"), inverseJoinColumns=@JoinColumn(name="follower_id"))
	private List<Client> following;
	
	public Client() {
		super();
	}
	
	public Client(Credentials credentials, Profile profile) {
		super();
		this.userName = credentials.getUserLogin();
		this.deleted = false;
		this.credentials = credentials;
		this.profile = profile;
		this.joined = new Timestamp(System.currentTimeMillis());
	}

	public Client(boolean deleted, Credentials credentials, Profile profile) {
		super();
		this.userName = credentials.getUserLogin();
		this.deleted = deleted;
		this.credentials = credentials;
		this.profile = profile;
		this.joined = new Timestamp(System.currentTimeMillis());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Timestamp getJoined() {
		return joined;
	}

	public void setJoined(Timestamp joined) {
		this.joined = joined;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public List<Client> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Client> followers) {
		this.followers = followers;
	}

	public List<Client> getFollowing() {
		return following;
	}

	public void setFollowing(List<Client> following) {
		this.following = following;
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
		if (!(obj instanceof Client)) {
			return false;
		}
		Client other = (Client) obj;
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
		return "Client [id=" + id + ", userName=" + userName + ", deleted=" + deleted + ", joined=" + joined
				+ ", profile=" + profile + ", credentials=" + credentials + ", followers=" + followers + ", following="
				+ following + "]";
	}
	
	

}
