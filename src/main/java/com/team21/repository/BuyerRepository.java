package com.team21.repository;

import org.springframework.data.repository.CrudRepository;

import com.team21.entity.BuyerEntity;

public interface BuyerRepository extends CrudRepository<BuyerEntity, String> {

	public BuyerEntity findByPhoneNumber(String phoneNumber);

	public BuyerEntity findByEmail(String email);

	public BuyerEntity findByBuyerId(String id);

}
