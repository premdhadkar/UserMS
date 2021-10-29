package com.team21.service;

import com.team21.dto.BuyerDTO;
import com.team21.exception.UserMSException;

public interface BuyerService {

	public String buyerRegistration(BuyerDTO buyerDTO) throws UserMSException;

}
