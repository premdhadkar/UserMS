package com.team21.validator;

import com.team21.dto.BuyerDTO;
import com.team21.dto.SellerDTO;
import com.team21.exception.UserMSException;

public class UserValidator {

	private UserValidator() {
		super();
	}

	public static void validateBuyer(BuyerDTO buyer) throws UserMSException {

		if (!validateName(buyer.getName()))
			throw new UserMSException("Validator.INVALID_NAME");

		if (!validateEmail(buyer.getEmail()))
			throw new UserMSException("Validator.INVALID_EMAIL");

		if (!validateContactNumber(buyer.getPhoneNumber()))
			throw new UserMSException("Validator.INVALID_NUMBER");

		if (!validatePassword(buyer.getPassword()))
			throw new UserMSException("Validator.INVALID_PASSWORD");

	}

	public static void validateSeller(SellerDTO buyer) throws UserMSException {

		if (!validateName(buyer.getName()))
			throw new UserMSException("Validator.INVALID_NAME");

		if (!validateEmail(buyer.getEmail()))
			throw new UserMSException("Validator.INVALID_EMAIL");

		if (!validateContactNumber(buyer.getPhoneNumber()))
			throw new UserMSException("Validator.INVALID_NUMBER");

		if (!validatePassword(buyer.getPassword()))
			throw new UserMSException("Validator.INVALID_PASSWORD");

	}

	public static boolean validateName(String name) {

		String regex = "[A-Za-z]+([ ][A-Za-z]+){0,10}";

		return name.matches(regex);

	}

	public static boolean validateEmail(String email) {
		String regex = "[A-Za-z]+@[A-Za-z]+[\\.]com";

		return email.matches(regex);
	}

	public static boolean validateContactNumber(String contactNumber) {

		String regex = "[6-9][0-9]{9}";

		return contactNumber.matches(regex);
	}

	public static boolean validatePassword(String password) {
		String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{7,20}$";

		return password.matches(regex);
	}

}
