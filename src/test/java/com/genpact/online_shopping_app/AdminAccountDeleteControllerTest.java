package com.genpact.online_shopping_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.genpact.online_shopping_app.controller.AdminAccountDeleteController;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.Role;
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

@ExtendWith(MockitoExtension.class)
class AdminAccountDeleteControllerTest {

	@Mock
	UserService userService;
	@Mock
	VendorService vendorService;
	@Mock
	ProductService productService;
	@Mock
	CartService cartService;
	@Mock
	OrderService orderService;
	@Mock
	AddressService addressService;
	@Mock
	AuthService authService;
	@Mock
	HttpSession session;

	private AdminAccountDeleteController controller;

	@BeforeEach
	void setUp() {
		controller = new AdminAccountDeleteController(userService, vendorService, productService, cartService,
				orderService, addressService, authService);
	}

	@Test
	void deleteUser_requiresAdminSession() throws Exception {
		when(session.getAttribute("activeAdmin")).thenReturn(null);

		ResponseEntity<Map<String, Object>> response = controller.deleteUser(1, "Fraud", session);

		assertEquals(401, response.getStatusCode().value());
		assertFalse((Boolean) response.getBody().get("success"));
		verify(userService, never()).removeUserById(anyInt());
	}

	@Test
	void deleteUser_requiresReason() throws Exception {
		when(session.getAttribute("activeAdmin")).thenReturn(true);

		ResponseEntity<Map<String, Object>> response = controller.deleteUser(1, "  ", session);

		assertEquals(400, response.getStatusCode().value());
		verify(userService, never()).removeUserById(anyInt());
	}

	@Test
	void deleteUser_removesRelatedDataAndBlocksEmail() throws Exception {
		when(session.getAttribute("activeAdmin")).thenReturn(true);
		User user = new User("Asha", "asha@example.com", "hash", "999", new Role("user"));
		user.setUserId(7);
		when(userService.findById(7)).thenReturn(Optional.of(user));
		when(cartService.viewAllCarts()).thenReturn(List.of());

		ResponseEntity<Map<String, Object>> response = controller.deleteUser(7, "Spam", session);

		assertEquals(200, response.getStatusCode().value());
		assertTrue((Boolean) response.getBody().get("success"));
		verify(orderService).removeOrdersByVirtualId(7);
		verify(addressService).deleteByVirtualId(7);
		verify(userService).removeUserById(7);
		verify(authService).blockEmail("asha@example.com", "USER", "Spam");
	}

	@Test
	void deleteVendor_firstOffense_warnsOnly() throws Exception {
		when(session.getAttribute("activeAdmin")).thenReturn(true);
		Vendor vendor = new Vendor("Shop", "v@example.com", "hash", "111", new Role("vendor"));
		vendor.setVendorId(3);
		vendor.setWarningCount(0);
		when(vendorService.findById(3)).thenReturn(Optional.of(vendor));

		ResponseEntity<Map<String, Object>> response = controller.deleteVendor(3, "Complaints", session);

		assertEquals(200, response.getStatusCode().value());
		assertTrue((Boolean) response.getBody().get("warned"));
		assertFalse((Boolean) response.getBody().get("deleted"));
		assertEquals(1, vendor.getWarningCount());
		verify(vendorService).save(vendor);
		verify(vendorService, never()).removeVendorById(anyInt());
		verify(authService, never()).blockEmail(any(), any(), any());
	}

	@Test
	void deleteVendor_secondOffense_deletesProductsAndBlocksEmail() throws Exception {
		when(session.getAttribute("activeAdmin")).thenReturn(true);
		Vendor vendor = new Vendor("Shop", "v@example.com", "hash", "111", new Role("vendor"));
		vendor.setVendorId(3);
		vendor.setWarningCount(1);
		when(vendorService.findById(3)).thenReturn(Optional.of(vendor));

		Product product = new Product("electronics", "Phone", "desc", 100, 5, 3, "p.png");
		product.setProductId(9);
		when(productService.viewAllByVirtualId(3)).thenReturn(List.of(product));
		when(cartService.viewAllCarts()).thenReturn(List.of());

		ResponseEntity<Map<String, Object>> response = controller.deleteVendor(3, "Fraud", session);

		assertEquals(200, response.getStatusCode().value());
		assertTrue((Boolean) response.getBody().get("deleted"));
		assertFalse((Boolean) response.getBody().get("warned"));
		assertEquals(1, response.getBody().get("productsRemoved"));
		verify(productService).deleteProductById(9);
		verify(vendorService).removeVendorById(3);
		verify(authService).blockEmail(eq("v@example.com"), eq("VENDOR"), eq("Fraud"));
	}
}
