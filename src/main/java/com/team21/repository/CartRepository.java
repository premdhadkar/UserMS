package com.team21.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.team21.entity.CartEntity;
import com.team21.utility.CompositeKey;

public interface CartRepository extends CrudRepository<CartEntity, CompositeKey> {

	public List<CartEntity> findByCompoundKeyBuyerId(String id);

	public void deleteByCompoundKeyBuyerIdAndCompoundKeyProdId(String buyId, String prodId);

	public CartEntity findByCompoundKeyBuyerIdAndCompoundKeyProdId(String buyId, String ProdId);

}
