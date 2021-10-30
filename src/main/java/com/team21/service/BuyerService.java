package com.team21.service;

import java.util.List;

import com.team21.dto.BuyerDTO;
import com.team21.dto.CartDTO;
import com.team21.exception.UserMSException;

public interface BuyerService {

	public String buyerRegistration(BuyerDTO buyerDTO) throws UserMSException;

	public String buyerLogin(String email, String password) throws UserMSException;

	public String deleteBuyer(String id) throws UserMSException;

	public String addToWishlist(String prodId, String buyerId) throws UserMSException;

	public String addToCart(String prodId, String buyerId, Integer quantity);

	public List<CartDTO> getCart(String id) throws UserMSException;

	public String removeFromCart(String buyerId, String prodId) throws UserMSException;

	public String updateBonusPoints(String buyerId, Integer rewPoints) throws UserMSException;

	public String removeFromWishlist(String buyerId, String prodId) throws UserMSException;

	public String moveFromWishlistToCart(String buyerId, String prodId, Integer quantity) throws UserMSException;

}
