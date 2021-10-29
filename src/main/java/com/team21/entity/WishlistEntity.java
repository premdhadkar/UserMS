package com.team21.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.team21.utility.CompositeKey;

@Entity
@Table(name = "wishlist")
public class WishlistEntity {

	// Embedded CompoundKey
	@EmbeddedId
	private CompositeKey compoundId;

	// Getters and Setters
	public CompositeKey getCompoundId() {
		return compoundId;
	}

	public void setCompoundId(CompositeKey compoundId) {
		this.compoundId = compoundId;
	}

}
