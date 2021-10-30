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

		SellerEntity seller = sellerRepository.findByEmail(sellerDTO.getEmail());

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

	// Seller Login
	@Override
	public String sellerLogin(String email, String password) throws UserMSException {

		if (!UserValidator.validateEmail(email))
			throw new UserMSException("You have entered wrong emailid!");

		SellerEntity seller = sellerRepository.findByEmail(email);

		if (seller == null)
			throw new UserMSException("Seller does not exist!");

		if (!seller.getPassword().equals(password))
			throw new UserMSException("Wrong credentials!");

		seller.setIsActive("True");

		sellerRepository.save(seller);

		return "Logged in successfully!";
	}

	// Delete Seller
	@Override
	public String deleteSeller(String id) throws UserMSException {

		SellerEntity seller = sellerRepository.findBySellerId(id);

		if (seller == null)
			throw new UserMSException("Seller does not exist!");

		sellerRepository.delete(seller);

		return "Account deleted successfully!";
	}

}
