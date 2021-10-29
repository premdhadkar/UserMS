package com.team21.utility;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class CompositeKey implements Serializable {

	protected String prodId;
	protected String buyerId;

	// Constructors
	public CompositeKey(String prodId, String buyerId) {
		super();
		this.prodId = prodId;
		this.buyerId = buyerId;
	}

	public CompositeKey() {
		super();
	}

	// Getters and Setters
	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(buyerId, prodId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKey other = (CompositeKey) obj;
		return Objects.equals(buyerId, other.buyerId) && Objects.equals(prodId, other.prodId);
	}

}
