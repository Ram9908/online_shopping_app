package com.genpact.online_shopping_app.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.service.AuthService;
import com.genpact.online_shopping_app.util.FlashMessages;

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

		if (authService.isEmailBlocked(email) && role != null && (role == 101 || role == 102)) {
			FlashMessages.error(session, "This email has been blocked and can no longer be used to log in.");
			mv.setViewName("redirect:/login.jsp");
			return mv;
		}

		if (role == 101) {
			Optional<User> user = authService.authenticateUser(email, password);
			if (user.isPresent()) {
				request.changeSessionId();
				session.setAttribute("activeUser", user.get());
				FlashMessages.success(session, "Logged in successfully.");
				mv.setViewName("redirect:/user");
				return mv;
			}
		} else if (role == 102) {
			Optional<Vendor> vendor = authService.authenticateVendor(email, password);
			if (vendor.isPresent()) {
				request.changeSessionId();
				session.setAttribute("activeVendor", vendor.get());
				FlashMessages.success(session, "Logged in successfully.");
				mv.setViewName("redirect:/vendor");
				return mv;
			}
		} else if (authService.authenticateAdmin(email, password)) {
			request.changeSessionId();
			session.setAttribute("activeAdmin", Boolean.TRUE);
			FlashMessages.success(session, "Logged in successfully.");
			mv.setViewName("redirect:/admin");
			return mv;
		}

		FlashMessages.error(session, "Invalid email, password, or role.");
		mv.setViewName("redirect:/login.jsp");
		return mv;
	}
}
