package com.genpact.online_shopping_app.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.repository.UserRepository;
import com.genpact.online_shopping_app.repository.VendorRepository;

/**
 * Credential checks. Passwords are stored as BCrypt hashes. Accounts created before the
 * hashing change (plain-text passwords) still log in once and are upgraded to a hash.
 */
@Service
public class AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	private final UserRepository userRepository;
	private final VendorRepository vendorRepository;
	private final PasswordEncoder encoder;
	private final String adminEmail;
	private final String adminPasswordHash;

	public AuthService(UserRepository userRepository, VendorRepository vendorRepository, PasswordEncoder encoder,
			@Value("${app.admin.email}") String adminEmail, @Value("${app.admin.password:}") String adminPassword) {
		this.userRepository = userRepository;
		this.vendorRepository = vendorRepository;
		this.encoder = encoder;
		this.adminEmail = adminEmail;
		String effective = adminPassword;
		if (effective == null || effective.isBlank()) {
			byte[] bytes = new byte[12];
			new SecureRandom().nextBytes(bytes);
			effective = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
			log.warn("ADMIN_PASSWORD not set. Generated admin login for this run -> {} / {}", adminEmail, effective);
		}
		this.adminPasswordHash = encoder.encode(effective);
	}

	public Optional<User> authenticateUser(String email, String rawPassword) {
		if (email == null || rawPassword == null) {
			return Optional.empty();
		}
		return userRepository.findFirstByEmail(email)
				.filter(u -> verify(rawPassword, u.getPassword(), hash -> {
					u.setPassword(hash);
					userRepository.save(u);
				}));
	}

	public Optional<Vendor> authenticateVendor(String email, String rawPassword) {
		if (email == null || rawPassword == null) {
			return Optional.empty();
		}
		return vendorRepository.findFirstByEmail(email)
				.filter(v -> verify(rawPassword, v.getPassword(), hash -> {
					v.setPassword(hash);
					vendorRepository.save(v);
				}));
	}

	public boolean authenticateAdmin(String email, String rawPassword) {
		return email != null && rawPassword != null && adminEmail.equalsIgnoreCase(email.trim())
				&& encoder.matches(rawPassword, adminPasswordHash);
	}

	public boolean emailInUse(String email) {
		return userRepository.existsByEmail(email) || vendorRepository.existsByEmail(email);
	}

	public String hash(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	private boolean verify(String raw, String stored, Consumer<String> upgradeHash) {
		if (stored == null) {
			return false;
		}
		if (stored.startsWith("$2")) {
			return encoder.matches(raw, stored);
		}
		// legacy plain-text account: constant-time compare, then upgrade to BCrypt
		boolean legacyMatch = MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8),
				raw.getBytes(StandardCharsets.UTF_8));
		if (legacyMatch) {
			upgradeHash.accept(encoder.encode(raw));
		}
		return legacyMatch;
	}
}
