package com.genpact.online_shopping_app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.service.AddressService;
import com.genpact.online_shopping_app.service.AuthService;
import com.genpact.online_shopping_app.service.CartService;
import com.genpact.online_shopping_app.service.OrderService;
import com.genpact.online_shopping_app.service.ProductService;
import com.genpact.online_shopping_app.service.UserService;
import com.genpact.online_shopping_app.service.VendorService;

import jakarta.servlet.http.HttpSession;

@RestController
public class AdminAccountDeleteController {

	private final UserService userService;
	private final VendorService vendorService;
	private final ProductService productService;
	private final CartService cartService;
	private final OrderService orderService;
	private final AddressService addressService;
	private final AuthService authService;

	public AdminAccountDeleteController(UserService userService, VendorService vendorService,
			ProductService productService, CartService cartService, OrderService orderService,
			AddressService addressService, AuthService authService) {
		this.userService = userService;
		this.vendorService = vendorService;
		this.productService = productService;
		this.cartService = cartService;
		this.orderService = orderService;
		this.addressService = addressService;
		this.authService = authService;
	}

	@PostMapping("/add/user/remove/{userId}")
	public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer userId,
			@RequestParam String reason, HttpSession session) {
		Map<String, Object> body = new HashMap<>();
		if (session.getAttribute("activeAdmin") == null) {
			body.put("success", false);
			body.put("message", "Please login as admin.");
			return ResponseEntity.status(401).body(body);
		}
		if (reason == null || reason.trim().isEmpty()) {
			body.put("success", false);
			body.put("message", "Please select a reason to delete this user.");
			return ResponseEntity.badRequest().body(body);
		}
		try {
			User user = userService.findById(userId).orElse(null);
			if (user == null) {
				body.put("success", false);
				body.put("message", "User not found.");
				return ResponseEntity.badRequest().body(body);
			}
			String email = user.getEmail();
			String trimmedReason = reason.trim();

			// Remove carts belonging to this user.
			List<Cart> carts = cartService.viewAllCarts();
			if (carts != null) {
				List<Integer> cartIds = new ArrayList<>();
				for (Cart cart : carts) {
					if (cart != null && userId.equals(cart.getVirtualId())) {
						cartIds.add(cart.getCartId());
					}
				}
				for (Integer cartId : cartIds) {
					cartService.deleteCartById(cartId);
				}
			}

			// Remove all orders and addresses for this user.
			orderService.removeOrdersByVirtualId(userId);
			addressService.deleteByVirtualId(userId);

			userService.removeUserById(userId);
			authService.blockEmail(email, "USER", trimmedReason);

			body.put("success", true);
			body.put("message", "User deleted and email blocked permanently. Reason: " + trimmedReason);
			return ResponseEntity.ok(body);
		} catch (Exception e) {
			body.put("success", false);
			body.put("message", e.getMessage() == null ? "Could not delete user." : e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	@PostMapping("/add/vendor/remove/{vendorId}")
	public ResponseEntity<Map<String, Object>> deleteVendor(@PathVariable Integer vendorId,
			@RequestParam String reason, HttpSession session) {
		Map<String, Object> body = new HashMap<>();
		if (session.getAttribute("activeAdmin") == null) {
			body.put("success", false);
			body.put("message", "Please login as admin.");
			return ResponseEntity.status(401).body(body);
		}
		if (reason == null || reason.trim().isEmpty()) {
			body.put("success", false);
			body.put("message", "Please select a reason.");
			return ResponseEntity.badRequest().body(body);
		}
		try {
			Vendor vendor = vendorService.findById(vendorId).orElse(null);
			if (vendor == null) {
				body.put("success", false);
				body.put("message", "Vendor not found.");
				return ResponseEntity.badRequest().body(body);
			}

			String trimmedReason = reason.trim();

			// First offense: warn only. Second offense: delete.
			if (vendor.getWarningCount() < 1) {
				vendor.setWarningCount(1);
				vendorService.save(vendor);
				body.put("success", true);
				body.put("warned", true);
				body.put("deleted", false);
				body.put("message", "Warning issued to vendor. Next violation will remove their account. Reason: "
						+ trimmedReason);
				return ResponseEntity.ok(body);
			}

			String email = vendor.getEmail();

			// Remove this vendor's products (and cart lines that reference them), then the vendor.
			List<Product> vendorProducts = productService.viewAllByVirtualId(vendorId);
			List<Integer> productIds = new ArrayList<>();
			if (vendorProducts != null) {
				for (Product product : vendorProducts) {
					if (product != null && product.getProductId() != null) {
						productIds.add(product.getProductId());
					}
				}
			}
			List<Cart> carts = cartService.viewAllCarts();
			if (carts != null && !productIds.isEmpty()) {
				List<Integer> cartIdsToRemove = new ArrayList<>();
				for (Cart cart : carts) {
					if (cart != null && cart.getvProductId() != null
							&& productIds.contains(cart.getvProductId())) {
						cartIdsToRemove.add(cart.getCartId());
					}
				}
				for (Integer cartId : cartIdsToRemove) {
					cartService.deleteCartById(cartId);
				}
			}
			for (Integer productId : productIds) {
				productService.deleteProductById(productId);
			}
			vendorService.removeVendorById(vendorId);
			authService.blockEmail(email, "VENDOR", trimmedReason);

			body.put("success", true);
			body.put("warned", false);
			body.put("deleted", true);
			body.put("productsRemoved", productIds.size());
			body.put("message", "Vendor deleted after prior warning (" + productIds.size()
					+ " product(s) removed). Email blocked permanently. Reason: " + trimmedReason);
			return ResponseEntity.ok(body);
		} catch (Exception e) {
			body.put("success", false);
			body.put("message", e.getMessage() == null ? "Could not process vendor action." : e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}
}
