package com.team21.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team21.dto.BuyerDTO;
import com.team21.entity.BuyerEntity;
import com.team21.exception.UserMSException;
import com.team21.repository.BuyerRepository;
import com.team21.validator.UserValidator;

@Transactional
@Service(value = "buyerService")
public class BuyerServiceImpl implements BuyerService {

	private static int buyerCount;
	private static int sellerCount;

	static {
		buyerCount = 100;
		sellerCount = 100;
	}

	@Autowired
	private BuyerRepository buyerRepository;

	@Override
	public String buyerRegistration(BuyerDTO buyerDTO) throws UserMSException {

		BuyerEntity buyer = buyerRepository.findByPhoneNumber(buyerDTO.getPhoneNumber());

		if (buyer != null)
			throw new UserMSException("Buyer already exist");

		UserValidator.validateBuyer(buyerDTO);

		String id = "BUY" + buyerCount++;

		buyer = new BuyerEntity();

		buyer.setBuyerId(id);
		buyer.setEmail(buyerDTO.getEmail());
		buyer.setName(buyerDTO.getName());
		buyer.setPhoneNumber(buyerDTO.getPhoneNumber());
		buyer.setPassword(buyerDTO.getPassword());
		buyer.setIsActive("False");
		buyer.setIsPrivileged("False");
		buyer.setRewardPoints("0");

		buyerRepository.save(buyer);

		return buyer.getBuyerId();

	}

}
