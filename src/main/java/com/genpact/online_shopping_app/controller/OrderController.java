package com.genpact.online_shopping_app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.entity.Address;
import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.service.CheckoutService;
import com.genpact.online_shopping_app.service.OrderService;
import com.genpact.online_shopping_app.util.FlashMessages;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	private final OrderService orderService;
	private final CheckoutService checkoutService;

	public OrderController(OrderService orderService, CheckoutService checkoutService) {
		this.orderService = orderService;
		this.checkoutService = checkoutService;
	}

	@PostMapping("view/all/add/address")
	public ModelAndView addAddress(HttpSession session) {
		if (session.getAttribute("activeUser") == null) {
			return new ModelAndView("redirect:/login.jsp");
		}
		return new ModelAndView("address");
	}

	@PostMapping("view/all/add/order/submitAddress")
	public ModelAndView placedOrder(@RequestParam String name, @RequestParam String addressLine1,
			@RequestParam String addressLine2, @RequestParam String city, @RequestParam String state,
			@RequestParam String zipCode, HttpSession session) {
		User activeUser = (User) session.getAttribute("activeUser");
		if (activeUser == null) {
			return new ModelAndView("redirect:/login.jsp");
		}
		try {
			Address address = new Address(name, addressLine1, addressLine2, city, state, zipCode,
					activeUser.getUserId());
			checkoutService.checkout(activeUser.getUserId(), address);
			FlashMessages.success(session, "Order placed successfully.");
			return new ModelAndView("redirect:/ordersView");
		} catch (IllegalStateException e) {
			FlashMessages.error(session, e.getMessage());
			return new ModelAndView("redirect:/view/all/cart");
		}
	}

	/** Cancels one of the logged-in user's own orders. */
	@GetMapping("/delete/{orderId}")
	public String deleteOrder(@PathVariable Integer orderId,
			@RequestParam(required = false) String cancelReason, HttpSession session) {
		User activeUser = (User) session.getAttribute("activeUser");
		if (activeUser == null) {
			return "redirect:/login.jsp";
		}
		if (cancelReason == null || cancelReason.trim().isEmpty()) {
			FlashMessages.error(session, "Please select a reason to cancel the order.");
			return "redirect:/ordersView";
		}
		List<OrderEntity> mine = orderService.getOrdersByVirtualId(activeUser.getUserId());
		boolean owns = mine.stream().anyMatch(o -> o.getOrderId() == orderId);
		if (owns) {
			orderService.removeOrderById(orderId);
			FlashMessages.success(session, "Order cancelled. Reason: " + cancelReason.trim());
		} else {
			FlashMessages.error(session, "Order not found.");
		}
		return "redirect:/ordersView";
	}
}
