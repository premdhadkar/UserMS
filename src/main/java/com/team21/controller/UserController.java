package com.team21.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team21.dto.BuyerDTO;
import com.team21.exception.UserMSException;
import com.team21.service.BuyerService;

@RestController
public class UserController {

	@Autowired
	BuyerService buyerService;

	// Register the Buyer
	@PostMapping(value = "/userMS/buyer/register")
	public ResponseEntity<String> registerBuyer(@RequestBody BuyerDTO buyerDto) {

		try {
			String result = "Buyer registered successfully with buyer Id : " + buyerService.buyerRegistration(buyerDto);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (UserMSException e) {
			String errorMsg = e.getMessage();
			return new ResponseEntity<>(errorMsg, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// Login for Buyer
	@PostMapping(value = "/userMS/buyer/login/{email}/{password}")
	public ResponseEntity<String> loginBuyer(@PathVariable String email, @PathVariable String password) {
		try {
			String msg = buyerService.buyerLogin(email, password);
			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (UserMSException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// Delete Buyer
	@DeleteMapping(value = "/userMS/buyer/deregister/{id}")
	public ResponseEntity<String> deleteBuyerAccount(@PathVariable String id) {
		try {
			String result = buyerService.deleteBuyer(id);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (UserMSException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
