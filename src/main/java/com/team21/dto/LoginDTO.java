package com.team21.dto;

import java.util.Objects;

// Login DTO for User
public class LoginDTO {

	private String emailId;
	private String password;

	// Getters and Setters
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Utility Methods

	@Override
	public int hashCode() {
		return Objects.hash(emailId, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginDTO other = (LoginDTO) obj;
		return Objects.equals(emailId, other.emailId) && Objects.equals(password, other.password);
	}

	@Override
	public String toString() {
		return "LoginDTO [emailId=" + emailId + ", password=" + password + "]";
	}

}
