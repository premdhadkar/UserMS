package com.team21.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.team21.dto.BuyerDTO;
import com.team21.exception.UserMSException;
import com.team21.repository.BuyerRepository;

public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerRepository buyerRepository;

	@Override
	public String buyerRegistration(BuyerDTO buyerDTO) throws UserMSException {
		return null;
	}

}
