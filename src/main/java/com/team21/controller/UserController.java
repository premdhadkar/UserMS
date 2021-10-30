package com.team21.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.team21.dto.BuyerDTO;
import com.team21.dto.CartDTO;
import com.team21.dto.SellerDTO;
import com.team21.exception.UserMSException;
import com.team21.service.BuyerService;
import com.team21.service.SellerService;

@RestController
public class UserController {

	@Autowired
	BuyerService buyerService;

	@Autowired
	SellerService sellerService;

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

	// Add Product to Buyer's WishList
	@PostMapping(value = "/userMS/buyer/wishlist/add/{buyerId}/{prodId}")
	public ResponseEntity<String> addToWishlist(@PathVariable String buyerId, @PathVariable String prodId)
			throws UserMSException {
		try {
			/*
			 * Here we will use rest template to fetch the product from ProductMS and from
			 * that product we will fetch the product id if product is not found then we
			 * will throw an exception which is commented below for now. for example:-
			 * ProductDTO product = new
			 * RestTemplate().getForObject(prodUri+"/prodMS/getById/"+prodId,
			 * ProductDTO.class);
			 */
			String result = buyerService.addToWishlist(prodId, buyerId);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (Exception e) {
//			String newMsg = "Product invalid or Product already in wishlist";
//			if (e.getMessage().equals("404 null")) {
//				newMsg = "Product is unavailable or product id is invalid";
//			}
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, newMsg, e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// Add Product to Buyers's Cart
	@PostMapping(value = "/userMS/buyer/cart/add/{buyerId}/{prodId}/{quantity}")
	public ResponseEntity<String> addProductToCart(@PathVariable String buyerId, @PathVariable String prodId,
			@PathVariable Integer quantity) throws UserMSException {
		try {
			/*
			 * Here we will use rest template to fetch the product from ProductMS and from
			 * that product we will fetch the product id if product is not found then we
			 * will throw an exception which is commented below for now. for example:-
			 * ProductDTO product = new
			 * RestTemplate().getForObject(prodUri+"/prodMS/getById/"+prodId,
			 * ProductDTO.class);
			 */
			String result = buyerService.addToCart(prodId, buyerId, quantity);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (Exception e) {
//			String newMsg = "Product invalid or Product already in wishlist";
//			if (e.getMessage().equals("404 null")) {
//				newMsg = "Product is unavailable or product id is invalid";
//			}
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, newMsg, e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// Get List of Cart Items
	@GetMapping(value = "/userMS/buyer/cart/get/{buyerId}")
	public ResponseEntity<List<CartDTO>> getProductListFromCart(@PathVariable String buyerId) throws UserMSException {
		try {
			List<CartDTO> CartDTOs = buyerService.getCart(buyerId);
			return new ResponseEntity<>(CartDTOs, HttpStatus.ACCEPTED);
		} catch (UserMSException e) {
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	// Remove item from Cart
	@PostMapping(value = "/userMS/buyer/cart/remove/{buyerId}/{prodId}")
	public ResponseEntity<String> removeFromCart(@PathVariable String buyerId, @PathVariable String prodId)
			throws UserMSException {

		try {
			/*
			 * Here we will use rest template to fetch the product from ProductMS and from
			 * that product we will fetch the product id if product is not found then we
			 * will throw an exception which is commented below for now. for example:-
			 * ProductDTO product = new
			 * RestTemplate().getForObject(prodUri+"/prodMS/getById/"+prodId,
			 * ProductDTO.class);
			 */
			String result = buyerService.removeFromCart(buyerId, prodId);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (UserMSException e) {
			String errorMsg = e.getMessage();
			return new ResponseEntity<String>(errorMsg, HttpStatus.NOT_FOUND);

		}
	}

	// Remove item from Wishlist
	@PostMapping(value = "/userMS/buyer/wishlist/remove/{buyerId}/{prodId}")
	public ResponseEntity<String> removeFromWishlist(@PathVariable String buyerId, @PathVariable String prodId)
			throws UserMSException {

		try {
			/*
			 * Here we will use rest template to fetch the product from ProductMS and from
			 * that product we will fetch the product id if product is not found then we
			 * will throw an exception which is commented below for now. for example:-
			 * ProductDTO product = new
			 * RestTemplate().getForObject(prodUri+"/prodMS/getById/"+prodId,
			 * ProductDTO.class);
			 */
			String result = buyerService.removeFromWishlist(buyerId, prodId);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (UserMSException e) {
			String errorMsg = e.getMessage();
			return new ResponseEntity<String>(errorMsg, HttpStatus.NOT_FOUND);

		}
	}

	// Update Bonus Points
	@GetMapping(value = "/userMS/updateBonusPoints/{buyerId}/{bonPoints}")
	public ResponseEntity<String> updateBonusPoints(@PathVariable String buyerId, @PathVariable Integer bonPoints) {
		try {
			String result = buyerService.updateBonusPoints(buyerId, bonPoints);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	// Register the Seller
	@PostMapping(value = "/userMS/seller/register")
	public ResponseEntity<String> registerSeller(@RequestBody SellerDTO sellerDto) {

		try {
			String result = "Seller registered successfully with seller Id : "
					+ sellerService.sellerRegistration(sellerDto);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (UserMSException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}

	}

	// Login for Seller
	@PostMapping(value = "/userMS/seller/login/{email}/{password}")
	public ResponseEntity<String> loginSeller(@PathVariable String email, @PathVariable String password) {
		try {
			String result = sellerService.sellerLogin(email, password);
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (UserMSException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// Delete seller
	@DeleteMapping(value = "/userMS/seller/deregister/{id}")
	public ResponseEntity<String> deleteSellerAccount(@PathVariable String id) {

		try {
			String msg = sellerService.deleteSeller(id);
			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (UserMSException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
