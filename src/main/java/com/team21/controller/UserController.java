package com.team21.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.team21.dto.BuyerDTO;
import com.team21.dto.CartDTO;
import com.team21.dto.LoginDTO;
import com.team21.dto.OrderDTO;
import com.team21.dto.ProductDTO;
import com.team21.dto.ProductOrderedDTO;
import com.team21.dto.SellerDTO;
import com.team21.exception.UserMSException;
import com.team21.service.BuyerService;
import com.team21.service.SellerService;
import com.team21.utility.CurrentOrderStatus;

@RestController
public class UserController {

	@Autowired
	BuyerService buyerService;

	@Autowired
	SellerService sellerService;

	@Value("${product.uri}")
	String productUri;

	@Value("${order.uri}")
	String orderUri;

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
	@PostMapping(value = "/userMS/buyer/login")
	public ResponseEntity<String> loginBuyer(@RequestBody LoginDTO loginDTO) {
		try {
			String msg = buyerService.buyerLogin(loginDTO);
			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (UserMSException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/userMS/buyer/{buyerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BuyerDTO> getSepcificBuyerDetails(@PathVariable String buyerId) {
		try {
			BuyerDTO buyerDTO = buyerService.getSepcificBuyer(buyerId);
			return new ResponseEntity<BuyerDTO>(buyerDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	// Seller will be able to view orders placed on their products
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/userMS/seller/view/soldProducts/{sellerId}/{prodId}")
	public ResponseEntity<List<ProductOrderedDTO>> getSellerProducts(@PathVariable String sellerId,
			@PathVariable String productId) {
		try {

			List<ProductOrderedDTO> productOrdered = new RestTemplate()
					.getForObject(orderUri + "view/bySellersProducts" + sellerId + productId, List.class);
			return new ResponseEntity<List<ProductOrderedDTO>>(productOrdered, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
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
			ProductDTO productDTO = new RestTemplate().getForObject(productUri + "product/get/Id/" + prodId,
					ProductDTO.class);
			String result = buyerService.addToWishlist(productDTO.getProdId(), buyerId);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (HttpClientErrorException e) {
			String errorMsg = "Product is unavailable or product id is invalid";
			return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
		}
	}

	// Add Product to Buyers's Cart
	@PostMapping(value = "/userMS/buyer/cart/add/{buyerId}/{prodId}/{quantity}")
	public ResponseEntity<String> addProductToCart(@PathVariable String buyerId, @PathVariable String prodId,
			@PathVariable Integer quantity) throws UserMSException {
		try {
			/*
			 * Here we are using rest template to fetch the productDTO from ProductMS, and
			 * from that productDTO we will fetch the product id. If product is not found
			 * then we are throwing an exception
			 */
			ProductDTO productDTO = new RestTemplate().getForObject(productUri + "product/get/Id/" + prodId,
					ProductDTO.class);
			if (productDTO.getStock() < quantity)
				throw new UserMSException("Unable to process your request. Stock Insufficient, current stock value is "
						+ productDTO.getStock());
			String result = buyerService.addToCart(productDTO.getProdId(), buyerId, quantity);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (HttpClientErrorException e) {
			String errorMsg = "Product is unavailable or product id is invalid";
			return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
		}
	}

	// Get List of Cart Items
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/userMS/buyer/cart/get/{buyerId}")
	public ResponseEntity<List<CartDTO>> getProductListFromCart(@PathVariable String buyerId) throws UserMSException {
		try {
			List<CartDTO> CartDTOs = buyerService.getCart(buyerId);
			return new ResponseEntity<>(CartDTOs, HttpStatus.ACCEPTED);
		} catch (UserMSException e) {

			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/cart/product/{buyerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getProductIdListFromCart(@PathVariable String buyerId) {
		try {
			List<CartDTO> listCart = buyerService.getCart(buyerId);
			List<String> productList = new ArrayList<>();
			for (CartDTO cart : listCart) {
				productList.add(cart.getProdId());
			}
			return productList;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	// Get Quantity from cart by giving buyerId and product ID
	@GetMapping(value = "/cart/product/quantity/{buyerId}/{prodId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer getQuantityFromCart(@PathVariable String buyerId, @PathVariable String prodId)
			throws UserMSException {
		try {
			List<CartDTO> listCart = buyerService.getCart(buyerId);
			for (CartDTO cart : listCart) {
				if (cart.getProdId().equals(prodId))
					return cart.getQuantity();
			}
			return 0;
		} catch (UserMSException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	// Remove item from Cart
	@PostMapping(value = "/userMS/buyer/cart/remove/{buyerId}/{prodId}")
	public ResponseEntity<String> removeFromCart(@PathVariable String buyerId, @PathVariable String prodId)
			throws UserMSException {
		try {
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
			String result = buyerService.removeFromWishlist(buyerId, prodId);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (UserMSException e) {
			String errorMsg = e.getMessage();
			return new ResponseEntity<String>(errorMsg, HttpStatus.NOT_FOUND);

		}
	}

	// Move from Wishlist to cart
	@PostMapping(value = "/userMS/buyer/wishlist/cart/{buyerId}/{prodId}/{quantity}")
	public ResponseEntity<String> moveWishlistToCart(@PathVariable String buyerId, @PathVariable String prodId,
			@PathVariable Integer quantity) {
		try {
			ProductDTO productDTO = new RestTemplate().getForObject(productUri + "product/get/Id/" + prodId,
					ProductDTO.class);
			if (productDTO.getStock() < quantity)
				throw new UserMSException("Unable to process your request. Stock Insufficient, current stock value is "
						+ productDTO.getStock());
			String result = buyerService.moveFromWishlistToCart(buyerId, prodId, quantity);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (HttpClientErrorException e) {
			String errorMsg = "Product is unavailable or product id is invalid";
			return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);

		} catch (UserMSException e) {
			String errorMsg = e.getMessage();
			return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
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
	@PostMapping(value = "/userMS/seller/login")
	public ResponseEntity<String> loginSeller(@RequestBody LoginDTO loginDTO) {
		try {
			String result = sellerService.sellerLogin(loginDTO);
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

	@PostMapping(value = "/seller/products/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO) {
		try {
			/*
			 * Here we will use rest template to fetch the product from ProductMS and from
			 * that product we will fetch the product id if product is not found then we
			 * will throw an exception which is commented below for now. for example:-
			 * ProductDTO product = new
			 * RestTemplate().getForObject(prodUri+"/prodMS/getById/"+prodId,
			 * ProductDTO.class);
			 */

			// String response = new RestTemplate().postForObject(productUri, productDTO,
			// String.class);
			// return response;
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return null;
	}

	@GetMapping(value = "/seller/products/delete/{prodId}")
	public ResponseEntity<String> deleteProduct(@PathVariable String prodId) {
		try {
			new RestTemplate().delete(productUri + "product/delete/" + prodId);

			String response = "Deleted Successfully";
			return new ResponseEntity<String>(response, HttpStatus.NOT_FOUND);
		} catch (HttpClientErrorException e) {
			String errorMsg = "Product is unavailable or product id is invalid";
			return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
		}
	}

	// after discount add additional reward points
	@PostMapping(value = "/userMS/updateRewards/{buyerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean orderUpdate(@RequestBody Float amount, @PathVariable String buyerId) {
		buyerService.updateRewardPoints(buyerId);
		buyerService.addRewardPoints(buyerId, amount);
		return true;

	}

	// checkout cart products for final order
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/userMS/cart/checkout", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> checkout(@RequestBody OrderDTO orderDTO) {
		try {
			String result = new RestTemplate().postForObject(orderUri + "order/place", orderDTO, String.class);
			return new ResponseEntity<String>(result, HttpStatus.ACCEPTED);
		} catch (HttpClientErrorException e) {

			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	// get reward points for specific user
	@GetMapping(value = "/userMS/get/rewardPoints/{buyerId}")
	public Integer getRewardPoints(@PathVariable String buyerId) throws UserMSException {

		try {
			return buyerService.getRewardPoints(buyerId);
		} catch (UserMSException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Buyer NOT found", e);
		}

	}

	@GetMapping(value = "/userMS/seller/order/status/update/{orderId}/{status}")
	public ResponseEntity<String> orderStatusUpdate(@PathVariable String orderId,
			@PathVariable CurrentOrderStatus status) throws UserMSException {
		try {
			String result = new RestTemplate().postForObject(orderUri + "order/status/update/" + orderId + "/" + status,
					null, String.class);
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (HttpClientErrorException e) {

			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);

		}
	}

}
