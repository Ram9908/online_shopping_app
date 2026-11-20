package com.genpact.online_shopping_app.config;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Blocks direct access to role pages / vendor product form after sign-out,
 * including browser back-button hits that bypass Spring MVC interceptors.
 */
@Component
@Order(1)
public class SessionAuthFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		HttpSession session = request.getSession(false);

		if (isBlocked(path, "/product_details.jsp", "activeVendor", session)
				|| isBlocked(path, "/product/success", "activeVendor", session)
				|| isBlocked(path, "/user", "activeUser", session)
				|| isBlocked(path, "/vendor", "activeVendor", session)
				|| isBlocked(path, "/admin", "activeAdmin", session)) {
			preventCaching(response);
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private static boolean isBlocked(String path, String protectedPath, String attr, HttpSession session) {
		if (!protectedPath.equals(path)) {
			return false;
		}
		return session == null || session.getAttribute(attr) == null;
	}

	private static void preventCaching(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
	}
}
