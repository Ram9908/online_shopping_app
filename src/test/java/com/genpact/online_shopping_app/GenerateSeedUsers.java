package com.genpact.online_shopping_app;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateSeedUsers {

	public static void main(String[] args) throws Exception {
		String hash = new BCryptPasswordEncoder(10).encode("Password123");
		StringBuilder sql = new StringBuilder();
		sql.append("-- All seed users share password: Password123\n");
		sql.append("USE online_shopping_application;\n");
		for (int i = 1; i <= 100; i++) {
			String email = String.format("user%03d@example.com", i);
			String name = String.format("Test User %03d", i);
			String phone = String.format("9000000%03d", i);
			sql.append(String.format(
					"INSERT INTO `user` (email, Password, phone, user_name, role_id) VALUES ('%s', '%s', '%s', '%s', 101);%n",
					email, hash, phone, name));
		}
		Path out = Path.of("seed_100_users.sql");
		Files.writeString(out, sql.toString());
		System.out.println("Wrote " + out.toAbsolutePath());
		System.out.println("Login password for all: Password123");
	}
}