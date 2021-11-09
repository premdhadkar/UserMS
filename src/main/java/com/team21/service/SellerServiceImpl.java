package com.team21.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team21.dto.LoginDTO;
import com.team21.dto.SellerDTO;
import com.team21.entity.SellerEntity;
import com.team21.exception.UserMSException;
import com.team21.repository.SellerRepository;
import com.team21.validator.UserValidator;

@Transactional
@Service(value = "sellerService")
public class SellerServiceImpl implements SellerService {

	private static int sellerCount = 100;

	@Autowired
	private SellerRepository sellerRepository;

	// Seller Registration
	@Override
	public String sellerRegistration(SellerDTO sellerDTO) throws UserMSException {

		// find if a seller with same email
		SellerEntity sellerByEmail = sellerRepository.findByEmail(sellerDTO.getEmail());
		// find if a seller with same phone number
		SellerEntity sellerByPhoneNumber = sellerRepository.findByPhoneNumber(sellerDTO.getPhoneNumber());

		// using Demorgan's Law in programming logic
		if (!(sellerByEmail == null && sellerByPhoneNumber == null))
			throw new UserMSException("Service.SELLER_ALREADY_EXIST");

		UserValidator.validateSeller(sellerDTO);

		String id = "SELL" + sellerCount++;

		SellerEntity seller = new SellerEntity();

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
	public String sellerLogin(LoginDTO loginDTO) throws UserMSException {
		String email = loginDTO.getEmailId();
		String password = loginDTO.getPassword();

		if (!UserValidator.validateEmail(email))
			throw new UserMSException("Service.INVALID_EMAIL_ADDRESS");

		SellerEntity seller = sellerRepository.findByEmail(email);

		if (seller == null)
			throw new UserMSException("Service.SELLER_DOES_NOT_EXIST");

		if (!seller.getPassword().equals(password))
			throw new UserMSException("Service.WRONG_CREDENTIALS");

		seller.setIsActive("True");

		sellerRepository.save(seller);

		return "Logged in successfully!";
	}

	// Delete Seller
	@Override
	public String deleteSeller(String sellerId) throws UserMSException {

		SellerEntity seller = sellerRepository.findBySellerId(sellerId);

		if (seller == null)
			throw new UserMSException("Service.SELLER_DOES_NOT_EXIST");

		seller.setIsActive("False");

		sellerRepository.save(seller);

		return "Account Deactivation successfull!";
	}

	@Override
	public boolean isSellerPresent(String sellerId) throws UserMSException {
		if (!sellerRepository.findById(sellerId).isPresent())
			throw new UserMSException("Service.SELLER_DOES_NOT_EXIST");
		return true;
	}

}
