package com.genpact.online_shopping_app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.service.CartService;
import com.genpact.online_shopping_app.service.ProductService;

import jakarta.servlet.http.HttpSession;

@RestController
public class AdminProductDeleteController {

	private final ProductService productService;
	private final CartService cartService;

	public AdminProductDeleteController(ProductService productService, CartService cartService) {
		this.productService = productService;
		this.cartService = cartService;
	}

	@PostMapping("/view/product/admin/remove/{productId}")
	public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Integer productId,
			@RequestParam String reason, HttpSession session) {
		Map<String, Object> body = new HashMap<>();
		if (session.getAttribute("activeAdmin") == null) {
			body.put("success", false);
			body.put("message", "Please login as admin.");
			return ResponseEntity.status(401).body(body);
		}
		if (reason == null || reason.trim().isEmpty()) {
			body.put("success", false);
			body.put("message", "Please select a reason to delete this product.");
			return ResponseEntity.badRequest().body(body);
		}
		try {
			if (productService.viewByProductId(productId).isEmpty()) {
				body.put("success", false);
				body.put("message", "Product not found.");
				return ResponseEntity.badRequest().body(body);
			}
			List<Cart> carts = cartService.viewAllCarts();
			if (carts != null) {
				for (Cart cart : carts) {
					if (cart != null && productId.equals(cart.getvProductId())) {
						cartService.deleteCartById(cart.getCartId());
					}
				}
			}
			productService.deleteProductById(productId);
			body.put("success", true);
			body.put("message", "Product deleted. Reason: " + reason.trim());
			return ResponseEntity.ok(body);
		} catch (Exception e) {
			body.put("success", false);
			body.put("message", e.getMessage() == null ? "Could not delete product." : e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}
}
