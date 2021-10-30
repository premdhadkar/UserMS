package com.team21.repository;

import org.springframework.data.repository.CrudRepository;

import com.team21.entity.SellerEntity;

public interface SellerRepository extends CrudRepository<SellerEntity, String> {

	public SellerEntity findByPhoneNumber(String phoneNumber);

	public SellerEntity findByEmail(String email);

	public SellerEntity findBySellerId(String id);

}
