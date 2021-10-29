package com.team21.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team21.dto.BuyerDTO;
import com.team21.entity.BuyerEntity;
import com.team21.entity.WishlistEntity;
import com.team21.exception.UserMSException;
import com.team21.repository.BuyerRepository;
import com.team21.repository.WishlistRepository;
import com.team21.utility.CompositeKey;
import com.team21.validator.UserValidator;

@Transactional
@Service(value = "buyerService")
public class BuyerServiceImpl implements BuyerService {
	
	//To maintain Buyer's Id in sequential manner
	private static int buyerCount;

	static {
		buyerCount = 100;
	}

	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private WishlistRepository wishlistRepository;

	// Registration for Buyer
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

	// Login for Buyer
	@Override
	public String buyerLogin(String email, String password) throws UserMSException {
		if (!UserValidator.validateEmail(email))
			throw new UserMSException("You have entered wrong EmailId!");

		BuyerEntity buyer = buyerRepository.findByEmail(email);

		if (buyer == null)
			throw new UserMSException("Buyer does not exist!");

		if (!buyer.getPassword().equals(password))
			throw new UserMSException("Wrong credentials");

		buyer.setIsActive("True");

		buyerRepository.save(buyer);

		return "Logged in Successfully";
	}

	// Delete Buyer
	@Override
	public String deleteBuyer(String id) throws UserMSException {

		BuyerEntity buyer = buyerRepository.findByBuyerId(id);

		if (buyer == null)
			throw new UserMSException("Buyer does not exist!");

		buyerRepository.delete(buyer);

		return "Account Deleted Successfully";
	}

	// Add Product to Buyers's WishList
	@Override
	public String addToWishlist(String prodId, String buyerId) throws UserMSException {

		CompositeKey newWishCompositeKey = new CompositeKey(prodId, buyerId);

		Optional<WishlistEntity> optional = wishlistRepository.findById(newWishCompositeKey);
		
		if(optional.isPresent())
			throw new UserMSException("Product already present in Wishlist");
		
		WishlistEntity newWish = new WishlistEntity();

		newWish.setCompoundId(newWishCompositeKey);

		wishlistRepository.save(newWish);

		return "Added Successfully to Wishlist";
	}

}
