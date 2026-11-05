package com.genpact.online_shopping_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.genpact.online_shopping_app.entity.Role;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.repository.UserRepository;
import com.genpact.online_shopping_app.repository.VendorRepository;
import com.genpact.online_shopping_app.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private VendorRepository vendorRepository;

	private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);
	private AuthService authService;

	@BeforeEach
	void setUp() {
		authService = new AuthService(userRepository, vendorRepository, encoder, "admin@shop.local", "Adm1n-secret");
	}

	private User userWithPassword(String stored) {
		return new User("Asha", "asha@example.com", stored, "9999999999", new Role("user"));
	}

	@Test
	void userLogin_correctPassword_succeeds() {
		User user = userWithPassword(encoder.encode("pass123"));
		when(userRepository.findFirstByEmail("asha@example.com")).thenReturn(Optional.of(user));

		assertTrue(authService.authenticateUser("asha@example.com", "pass123").isPresent());
	}

	@Test
	void userLogin_wrongPassword_fails() {
		User user = userWithPassword(encoder.encode("pass123"));
		when(userRepository.findFirstByEmail("asha@example.com")).thenReturn(Optional.of(user));

		assertFalse(authService.authenticateUser("asha@example.com", "wrong").isPresent());
	}

	@Test
	void userLogin_unknownEmail_fails() {
		when(userRepository.findFirstByEmail("nobody@example.com")).thenReturn(Optional.empty());

		assertFalse(authService.authenticateUser("nobody@example.com", "pass123").isPresent());
	}

	@Test
	void legacyPlainTextPassword_logsInOnceAndIsUpgradedToBcrypt() {
		User legacy = userWithPassword("plain123");
		when(userRepository.findFirstByEmail("asha@example.com")).thenReturn(Optional.of(legacy));
		when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		assertTrue(authService.authenticateUser("asha@example.com", "plain123").isPresent());

		verify(userRepository).save(legacy);
		assertTrue(legacy.getPassword().startsWith("$2"));
		assertTrue(encoder.matches("plain123", legacy.getPassword()));
	}

	@Test
	void adminLogin_onlyWithConfiguredCredentials() {
		assertTrue(authService.authenticateAdmin("admin@shop.local", "Adm1n-secret"));
		assertFalse(authService.authenticateAdmin("admin@shop.local", "nope"));
		assertFalse(authService.authenticateAdmin("other@shop.local", "Adm1n-secret"));
	}

	@Test
	void hash_neverReturnsThePlainPassword() {
		String hashed = authService.hash("secret1");
		assertFalse(hashed.equals("secret1"));
		assertEquals(60, hashed.length());
	}
}
