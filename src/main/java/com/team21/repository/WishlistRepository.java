package com.team21.repository;

import org.springframework.data.repository.CrudRepository;

import com.team21.entity.WishlistEntity;
import com.team21.utility.CompositeKey;

public interface WishlistRepository extends CrudRepository<WishlistEntity, CompositeKey> {
	
	public WishlistEntity findByCompoundIdBuyerIdAndCompoundIdProdId(String buyId, String ProdId);

	public void deleteByCompoundIdBuyerIdAndCompoundIdProdId(String buyerId, String prodId);
}
