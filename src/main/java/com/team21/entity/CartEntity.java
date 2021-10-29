package com.team21.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.team21.utility.CompositeKey;

@Entity
@Table(name = "cart")
public class CartEntity {

	// Embedded CompoundKey
	@EmbeddedId
	private CompositeKey compoundKey;

	private Integer quantity;

	// Getters and Setters
	public CompositeKey getCompoundKey() {
		return compoundKey;
	}

	public void setCompoundKey(CompositeKey compoundKey) {
		this.compoundKey = compoundKey;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
