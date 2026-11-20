package com.genpact.online_shopping_app.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Serves product images at /Images/** and blocks protected pages unless the matching
 * role is logged in. After sign-out, session attributes are gone so these checks fail.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${app.upload-dir:uploads}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
		try {
			Files.createDirectories(dir);
		} catch (IOException e) {
			throw new IllegalStateException("Cannot create upload directory " + dir, e);
		}
		String location = dir.toUri().toString();
		if (!location.endsWith("/")) {
			location += "/";
		}
		registry.addResourceHandler("/Images/**").addResourceLocations(location, "/Images/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requireSessionAttr("activeAdmin")).addPathPatterns(
				"/admin",
				"/add/vendor",
				"/add/vendor/remove/**",
				"/add/user",
				"/add/user/remove/**",
				"/orders",
				"/view/product/admin",
				"/view/product/admin/remove/**");

		registry.addInterceptor(requireSessionAttr("activeUser")).addPathPatterns(
				"/user",
				"/view/all",
				"/view/add/cart/**",
				"/view/all/cart",
				"/view/all/delete/*",
				"/view/all/update/*",
				"/view/all/add/address",
				"/view/all/add/order/submitAddress",
				"/ordersView",
				"/products/category/**",
				"/productsearch",
				"/productsearch/**",
				"/delete/*");

		registry.addInterceptor(requireSessionAttr("activeVendor")).addPathPatterns(
				"/vendor",
				"/product",
				"/product/success",
				"/product_details.jsp",
				"/view/all/products",
				"/view/all/remove/product/**",
				"/view/all/update/product/**",
				"/p/**");
	}

	private static HandlerInterceptor requireSessionAttr(String attribute) {
		return new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				preventCaching(response);
				HttpSession session = request.getSession(false);
				if (session != null && session.getAttribute(attribute) != null) {
					return true;
				}
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				return false;
			}
		};
	}

	private static void preventCaching(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
	}
}
