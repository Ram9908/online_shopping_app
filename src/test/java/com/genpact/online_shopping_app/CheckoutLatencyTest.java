package com.genpact.online_shopping_app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.genpact.online_shopping_app.entity.Address;
import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.repository.AddressRepository;
import com.genpact.online_shopping_app.repository.CartRepository;
import com.genpact.online_shopping_app.repository.OrderRepository;
import com.genpact.online_shopping_app.repository.ProductRepository;
import com.genpact.online_shopping_app.service.CheckoutService;

/**
 * Measures checkout response time (median ms) for locked CheckoutService vs unlocked baseline.
 * MySQL only (no H2). Uses schema online_shopping_latency_test so app seed data is untouched.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CheckoutService.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CheckoutLatencyTest {

	private static final int WARMUP = 100;
	private static final int SAMPLES = 500;

	@Autowired
	CheckoutService checkoutService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	PlatformTransactionManager txManager;

	@DynamicPropertySource
	static void mysqlOnly(DynamicPropertyRegistry registry) {
		String user = firstNonBlank(System.getenv("DB_USERNAME"), readDotEnv("DB_USERNAME"), "root");
		String password = firstNonBlank(System.getenv("DB_PASSWORD"), readDotEnv("DB_PASSWORD"), "");
		registry.add("spring.datasource.url",
				() -> "jdbc:mysql://localhost:3307/online_shopping_latency_test?createDatabaseIfNotExist=true");
		registry.add("spring.datasource.username", () -> user);
		registry.add("spring.datasource.password", () -> password);
		registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
		registry.add("app.admin.email", () -> "admin@test.local");
		registry.add("app.admin.password", () -> "x");
	}

	private static String firstNonBlank(String... values) {
		if (values == null) {
			return "";
		}
		for (String v : values) {
			if (v != null && !v.isBlank()) {
				return v.trim();
			}
		}
		return "";
	}

	private static String readDotEnv(String key) {
		try {
			Path env = Path.of(".env");
			if (!Files.exists(env)) {
				return null;
			}
			for (String line : Files.readAllLines(env)) {
				String trimmed = line.trim();
				if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.startsWith(key + "=")) {
					continue;
				}
				return trimmed.substring(key.length() + 1).trim();
			}
		} catch (Exception ignored) {
			// ignore and fall back
		}
		return null;
	}

	@BeforeEach
	void clean() {
		orderRepository.deleteAll();
		cartRepository.deleteAll();
		addressRepository.deleteAll();
		productRepository.deleteAll();
	}

	private Address address(int userId) {
		Address a = new Address();
		a.setName("buyer" + userId);
		a.setAddressLine1("1 Test St");
		a.setCity("X");
		a.setState("Y");
		a.setZipCode("000000");
		a.setVirtualId(userId);
		return a;
	}

	private void naiveCheckout(Integer userId, Address address) {
		new TransactionTemplate(txManager).executeWithoutResult(status -> {
			List<Cart> carts = cartRepository.findAllByVirtualId(userId);
			Address saved = addressRepository.save(address);
			for (Cart cart : carts) {
				Product product = productRepository.findById(cart.getvProductId()).orElseThrow();
				int qty = cart.getQuantity();
				if (product.getStockQuantity() < qty) {
					throw new IllegalStateException("out of stock");
				}
				product.setStockQuantity(product.getStockQuantity() - qty);
				productRepository.save(product);
				orderRepository.save(new OrderEntity(LocalDateTime.now(), (float) (product.getPrice() * qty), userId,
						cart.getCartId(), product.getProductId(), saved.getId()));
				cartRepository.delete(cart);
			}
		});
	}

	private double medianMs(boolean locked) {
		int total = WARMUP + SAMPLES;
		Product p = new Product();
		p.setCategory("test");
		p.setProductName("Latency widget");
		p.setProductDescription("timing");
		p.setPrice(100);
		p.setStockQuantity(total + 10);
		p.setVirtualId(1);
		p.setImageUrl("latency.png");
		p = productRepository.save(p);
		for (int i = 1; i <= total; i++) {
			Cart c = new Cart();
			c.setvProductId(p.getProductId());
			c.setVirtualId(5000 + i);
			c.setQuantity(1);
			cartRepository.save(c);
		}
		List<Double> times = new ArrayList<>();
		for (int i = 1; i <= total; i++) {
			int userId = 5000 + i;
			Address a = address(userId);
			long t0 = System.nanoTime();
			if (locked) {
				checkoutService.checkout(userId, a);
			} else {
				naiveCheckout(userId, a);
			}
			double ms = (System.nanoTime() - t0) / 1_000_000.0;
			if (i > WARMUP) {
				times.add(ms);
			}
		}
		clean();
		Collections.sort(times);
		return times.get(times.size() / 2);
	}

	@Test
	void measureCheckoutResponseTime() {
		double naiveA = medianMs(false);
		double lockedA = medianMs(true);
		double lockedB = medianMs(true);
		double naiveB = medianMs(false);
		double naive = (naiveA + naiveB) / 2;
		double locked = (lockedA + lockedB) / 2;
		double improvement = 100.0 * (naive - locked) / naive;

		System.out.println("==================== RESPONSE TIME RESULTS ====================");
		System.out.printf("Samples per run: %d (after %d warm-up), single buyer, MySQL (no H2)%n", SAMPLES, WARMUP);
		System.out.printf("Median checkout, NAIVE (no lock): %.3f ms%n", naive);
		System.out.printf("Median checkout, LOCKED (real)  : %.3f ms%n", locked);
		System.out.printf("Response-time change (positive = locked faster): %.1f%%%n", improvement);
		System.out.println("===============================================================");
	}
}
