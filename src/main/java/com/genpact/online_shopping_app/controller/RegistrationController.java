package com.genpact.online_shopping_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.entity.Role;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.RoleRepository;
import com.genpact.online_shopping_app.service.AuthService;
import com.genpact.online_shopping_app.service.UserService;
import com.genpact.online_shopping_app.service.VendorService;

@Controller
public class RegistrationController {

	private final UserService userService;
	private final VendorService vendorService;
	private final AuthService authService;
	private final RoleRepository roleRepository;

	public RegistrationController(UserService userService, VendorService vendorService, AuthService authService,
			RoleRepository roleRepository) {
		this.userService = userService;
		this.vendorService = vendorService;
		this.authService = authService;
		this.roleRepository = roleRepository;
	}

	@PostMapping("/signup")
	public ModelAndView registerUser(@RequestParam String name, @RequestParam String email,
			@RequestParam String phone, @RequestParam String password, @RequestParam String role) {
		ModelAndView mv = new ModelAndView();
		if (name.isBlank() || email.isBlank() || password.length() < 6) {
			mv.addObject("error", "Name and email are required and the password needs at least 6 characters.");
			mv.setViewName("errorPage_view");
			return mv;
		}
		if (authService.emailInUse(email)) {
			mv.addObject("error", "Email id already exists");
			mv.setViewName("errorPage_view");
			return mv;
		}
		try {
			String hashed = authService.hash(password);
			if ("User".equals(role)) {
				Role r = roleRepository.findById(101).orElseThrow();
				userService.registerUser(new User(name, email, hashed, phone, r));
			} else {
				Role r = roleRepository.findById(102).orElseThrow();
				vendorService.register(new Vendor(name, email, hashed, phone, r));
			}
			mv.addObject("successMessage", "Registration successful! You can now login.");
			mv.addObject("redirectUrl", "/login.jsp");
			mv.setViewName("success");
		} catch (OnlineShoppingException e) {
			mv.addObject("error", "An error occurred during registration: " + e.getMessage());
			mv.setViewName("errorPage_view");
		}
		return mv;
	}
}
