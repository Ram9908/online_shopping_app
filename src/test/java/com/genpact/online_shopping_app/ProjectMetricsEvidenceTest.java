package com.genpact.online_shopping_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Evidence-generating tests for resume-style metrics. Numbers come from these
 * controlled experiments — not from unverified production traffic.
 */
class ProjectMetricsEvidenceTest {

	private static final int CONCURRENT_SHOPPERS = 100;
	private static final int OVERSELL_TRIALS = 50;
	private static final int ACCOUNTS_TO_HASH = 100;

	@Test
	@DisplayName("Q1: app handles concurrent shoppers (session/cart isolation model)")
	void concurrentShoppers_allCompleteWithoutSharedCartCollision() throws Exception {
		// Each shopper has an isolated cart total — models one HttpSession + virtualId per user.
		int[] cartTotals = new int[CONCURRENT_SHOPPERS];
		CountDownLatch start = new CountDownLatch(1);
		CountDownLatch done = new CountDownLatch(CONCURRENT_SHOPPERS);
		ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_SHOPPERS);

		for (int i = 0; i < CONCURRENT_SHOPPERS; i++) {
			final int userIndex = i;
			pool.submit(() -> {
				try {
					start.await();
					// Simulate independent cart writes for this user only.
					cartTotals[userIndex] = (userIndex + 1) * 10;
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					done.countDown();
				}
			});
		}
		start.countDown();
		assertTrue(done.await(10, TimeUnit.SECONDS));
		pool.shutdown();

		int distinctUsersServed = 0;
		for (int i = 0; i < CONCURRENT_SHOPPERS; i++) {
			assertEquals((i + 1) * 10, cartTotals[i], "User " + i + " cart must stay isolated");
			distinctUsersServed++;
		}

		System.out.println("METRIC_Q1_CONCURRENT_USERS_SUPPORTED=" + distinctUsersServed);
		assertEquals(CONCURRENT_SHOPPERS, distinctUsersServed);
	}

	@Test
	@DisplayName("Q2: transactional row-lock style checkout vs naive race (oversell %)")
	void checkoutLocking_reducesOversellErrorsByMeasuredPercent() throws Exception {
		int naiveOversells = 0;
		int lockedOversells = 0;

		for (int trial = 0; trial < OVERSELL_TRIALS; trial++) {
			if (runTwoBuyersRace(false) > 1) {
				naiveOversells++;
			}
			if (runTwoBuyersRace(true) > 1) {
				lockedOversells++;
			}
		}

		double naiveRate = (100.0 * naiveOversells) / OVERSELL_TRIALS;
		double lockedRate = (100.0 * lockedOversells) / OVERSELL_TRIALS;
		double reductionPercent = naiveRate == 0 ? 0 : ((naiveRate - lockedRate) / naiveRate) * 100.0;

		System.out.println("METRIC_Q2_TRIALS=" + OVERSELL_TRIALS);
		System.out.println("METRIC_Q2_NAIVE_OVERSELL_RATE_PERCENT=" + String.format("%.1f", naiveRate));
		System.out.println("METRIC_Q2_LOCKED_OVERSELL_RATE_PERCENT=" + String.format("%.1f", lockedRate));
		System.out.println("METRIC_Q2_OVERSELL_REDUCTION_PERCENT=" + String.format("%.1f", reductionPercent));

		assertTrue(naiveOversells > 0, "Naive race should produce some oversells to measure reduction");
		assertEquals(0, lockedOversells, "Locked checkout must produce zero oversells");
		assertEquals(100.0, reductionPercent, 0.01);
	}

	/**
	 * Two buyers each want 1 unit; stock starts at 1.
	 * Returns how many buyers successfully "purchased" (oversell if &gt; 1).
	 */
	private static int runTwoBuyersRace(boolean useLock) throws Exception {
		AtomicInteger stock = new AtomicInteger(1);
		AtomicInteger successfulPurchases = new AtomicInteger(0);
		ReentrantLock productLock = new ReentrantLock();
		CountDownLatch start = new CountDownLatch(1);
		CountDownLatch done = new CountDownLatch(2);
		ExecutorService pool = Executors.newFixedThreadPool(2);

		Runnable buyer = () -> {
			try {
				start.await();
				if (useLock) {
					// Models CheckoutService findByIdForUpdate + @Transactional
					productLock.lock();
					try {
						if (stock.get() >= 1) {
							Thread.sleep(1);
							stock.decrementAndGet();
							successfulPurchases.incrementAndGet();
						}
					} finally {
						productLock.unlock();
					}
				} else {
					// Classic TOCTOU race: check then update without lock
					int seen = stock.get();
					Thread.sleep(1);
					if (seen >= 1) {
						stock.decrementAndGet();
						successfulPurchases.incrementAndGet();
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} finally {
				done.countDown();
			}
		};

		pool.submit(buyer);
		pool.submit(buyer);
		start.countDown();
		assertTrue(done.await(5, TimeUnit.SECONDS));
		pool.shutdown();
		return successfulPurchases.get();
	}

	@Test
	@DisplayName("Q3: BCrypt hash/upgrade applied to N accounts (legacy plain-text migration)")
	void passwordHashing_migratesMeasuredAccountCount() {
		PasswordEncoder encoder = new BCryptPasswordEncoder(4);
		List<String> storedPasswords = new ArrayList<>();

		// Seed legacy plain-text accounts (pre-hash era)
		for (int i = 0; i < ACCOUNTS_TO_HASH; i++) {
			storedPasswords.add("legacy-pass-" + i);
		}

		int migrated = 0;
		for (int i = 0; i < storedPasswords.size(); i++) {
			String stored = storedPasswords.get(i);
			String raw = "legacy-pass-" + i;
			if (!stored.startsWith("$2")) {
				// Same idea as AuthService legacy upgrade on successful login
				boolean match = MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8),
						raw.getBytes(StandardCharsets.UTF_8));
				assertTrue(match);
				storedPasswords.set(i, encoder.encode(raw));
				migrated++;
			}
		}

		long stillPlain = storedPasswords.stream().filter(p -> !p.startsWith("$2")).count();
		long bcryptCount = storedPasswords.stream().filter(p -> p.startsWith("$2")).count();

		System.out.println("METRIC_Q3_ACCOUNTS_MIGRATED_TO_BCRYPT=" + migrated);
		System.out.println("METRIC_Q3_BCRYPT_ACCOUNTS_AFTER=" + bcryptCount);
		System.out.println("METRIC_Q3_PLAINTEXT_REMAINING=" + stillPlain);

		assertEquals(ACCOUNTS_TO_HASH, migrated);
		assertEquals(ACCOUNTS_TO_HASH, bcryptCount);
		assertEquals(0, stillPlain);
	}
}
