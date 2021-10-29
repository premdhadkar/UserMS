package com.team21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team21.dto.BuyerDTO;
import com.team21.dto.CartDTO;
import com.team21.entity.BuyerEntity;
import com.team21.entity.CartEntity;
import com.team21.entity.WishlistEntity;
import com.team21.exception.UserMSException;
import com.team21.repository.BuyerRepository;
import com.team21.repository.CartRepository;
import com.team21.repository.WishlistRepository;
import com.team21.utility.CompositeKey;
import com.team21.validator.UserValidator;

@Transactional
@Service(value = "buyerService")
public class BuyerServiceImpl implements BuyerService {

	// To maintain Buyer's Id in sequential manner
	private static int buyerCount;

	static {
		buyerCount = 100;
	}

	@Autowired
	private BuyerRepository buyerRepository;

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private CartRepository cartRepository;

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

		CompositeKey productBuyerCompositeKey = new CompositeKey(prodId, buyerId);

		Optional<WishlistEntity> optional = wishlistRepository.findById(productBuyerCompositeKey);

		if (optional.isPresent())
			throw new UserMSException("Product already present in Wishlist");

		WishlistEntity newWish = new WishlistEntity();

		newWish.setCompoundId(productBuyerCompositeKey);

		wishlistRepository.save(newWish);

		return "Added Successfully to Wishlist";
	}

	// Add Product to Buyers's Cart
	@Override
	public String addToCart(String prodId, String buyerId, Integer quantity) {
		// if quantity to be added is zero
		if (quantity == 0)
			return "No Update needed";

		CompositeKey productBuyerCompositeKey = new CompositeKey(prodId, buyerId);

		Optional<CartEntity> optional = cartRepository.findById(productBuyerCompositeKey);

		CartEntity cart;

		if (optional.isPresent()) {

			cart = optional.get();

			quantity = quantity + cart.getQuantity();

		} else {

			cart = new CartEntity();

			cart.setCompoundKey(productBuyerCompositeKey);
		}

		cart.setQuantity(quantity);

		cartRepository.save(cart);

		return "Added Successfully to Cart";
	}

	// Get List of Cart Items
	@Override
	public List<CartDTO> getCart(String id) throws UserMSException {
		List<CartEntity> listOfProdsWithQty = cartRepository.findByCompoundKeyBuyerId(id);

		if (listOfProdsWithQty.isEmpty())
			throw new UserMSException("Cart is Empty");

		List<CartDTO> CartDTOs = new ArrayList<CartDTO>();

		for (CartEntity productFromCart : listOfProdsWithQty) {
			CartDTO cartDTO = new CartDTO();

			cartDTO.setBuyerId(productFromCart.getCompoundKey().getBuyerId());
			cartDTO.setProdId(productFromCart.getCompoundKey().getProdId());
			cartDTO.setQuantity(productFromCart.getQuantity());

			CartDTOs.add(cartDTO);
		}

		return CartDTOs;
	}

}
