package com.genpact.online_shopping_app.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private final AuthService authService;

	public LoginController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ModelAndView loginByRole(@RequestParam String email, @RequestParam String password,
			@RequestParam Integer role, HttpSession session, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		if (role == 101) {
			Optional<User> user = authService.authenticateUser(email, password);
			if (user.isPresent()) {
				request.changeSessionId(); // prevent session fixation
				session.setAttribute("activeUser", user.get());
				mv.setViewName("user_index");
				return mv;
			}
		} else if (role == 102) {
			Optional<Vendor> vendor = authService.authenticateVendor(email, password);
			if (vendor.isPresent()) {
				request.changeSessionId();
				session.setAttribute("activeVendor", vendor.get());
				mv.setViewName("vendor_index");
				return mv;
			}
		} else if (authService.authenticateAdmin(email, password)) {
			request.changeSessionId();
			session.setAttribute("activeAdmin", Boolean.TRUE);
			mv.setViewName("admin_index");
			return mv;
		}

		mv.addObject("error", "Invalid email, password, or role");
		mv.setViewName("error_view");
		return mv;
		// Add error handling for invalid role
	}
}
