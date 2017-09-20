package com.cooksys.tweeter.embedded;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class ClientData {

	@Embedded
	private Credentials credentials;
	
	@Embedded
	private Profile profile;
	
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
	
	public String getUserName(){
		return credentials.getUserLogin();
	}
	
	public String getPassword(){
		return credentials.getPassword();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((credentials == null) ? 0 : credentials.hashCode());
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
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
		if (!(obj instanceof ClientData)) {
			return false;
		}
		ClientData other = (ClientData) obj;
		if (credentials == null) {
			if (other.credentials != null) {
				return false;
			}
		} else if (!credentials.equals(other.credentials)) {
			return false;
		}
		if (profile == null) {
			if (other.profile != null) {
				return false;
			}
		} else if (!profile.equals(other.profile)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "ClientData [credentials=" + credentials + ", profile=" + profile + "]";
	}
	
}
