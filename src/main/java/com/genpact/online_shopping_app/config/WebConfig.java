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
 * Serves product images at /Images/**: first from the configurable upload directory
 * (vendor uploads), then from the bundled src/main/webapp/Images folder (sample images).
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

	/** Admin pages require an admin login (set by LoginController). */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				HttpSession session = request.getSession(false);
				if (session != null && session.getAttribute("activeAdmin") != null) {
					return true;
				}
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				return false;
			}
		}).addPathPatterns("/admin", "/add/vendor", "/add/user", "/orders", "/view/product/admin");
	}
}
