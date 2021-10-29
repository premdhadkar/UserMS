package com.team21.dto;

import java.util.Objects;

public class BuyerDTO {

	private String name;
	private String email;
	private String phoneNumber;
	private String password;
	private String isPrivileged;
	private String rewardPoints;
	private String isActive;

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIsPrivileged() {
		return isPrivileged;
	}

	public void setIsPrivileged(String isPrivileged) {
		this.isPrivileged = isPrivileged;
	}

	public String getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	// Utility Methods

	@Override
	public String toString() {
		return "BuyerDTO [name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber + ", password=" + password
				+ ", isPrivileged=" + isPrivileged + ", rewardPoints=" + rewardPoints + ", isActive=" + isActive + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, isActive, isPrivileged, name, password, phoneNumber, rewardPoints);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyerDTO other = (BuyerDTO) obj;
		return Objects.equals(email, other.email) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(isPrivileged, other.isPrivileged) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && Objects.equals(phoneNumber, other.phoneNumber)
				&& Objects.equals(rewardPoints, other.rewardPoints);
	}

}
