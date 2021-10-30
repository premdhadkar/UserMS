package com.team21.service;

import com.team21.dto.SellerDTO;
import com.team21.exception.UserMSException;

public interface SellerService {

	public String sellerRegistration(SellerDTO sellerDTO) throws UserMSException;

	public String sellerLogin(String email, String password) throws UserMSException;
}
