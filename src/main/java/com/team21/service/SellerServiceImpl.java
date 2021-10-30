package com.team21.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team21.dto.SellerDTO;
import com.team21.entity.SellerEntity;
import com.team21.exception.UserMSException;
import com.team21.repository.SellerRepository;
import com.team21.validator.UserValidator;

@Transactional
@Service(value = "sellerService")
public class SellerServiceImpl implements SellerService {

	private static int sellerCount;

	static {
		sellerCount = 100;

	}

	@Autowired
	private SellerRepository sellerRepository;

	// Seller Registration
	@Override
	public String sellerRegistration(SellerDTO sellerDTO) throws UserMSException {

		SellerEntity seller = sellerRepository.findByPhoneNumber(sellerDTO.getPhoneNumber());

		if (seller != null)
			throw new UserMSException("Seller Already exist!");
		
		UserValidator.validateSeller(sellerDTO);

		String id = "SELL" + sellerCount++;

		seller = new SellerEntity();

		seller.setEmail(sellerDTO.getEmail());
		seller.setSellerId(id);
		seller.setName(sellerDTO.getName());
		seller.setPassword(sellerDTO.getPassword());
		seller.setIsActive("False");
		seller.setPhoneNumber(sellerDTO.getPhoneNumber());

		sellerRepository.save(seller);

		return seller.getSellerId();
	}
}
