package com.genpact.online_shopping_app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateSeedCatalogAndOrders {

	public static void main(String[] args) throws Exception {
		String hash = new BCryptPasswordEncoder(10).encode("Password123");
		StringBuilder sql = new StringBuilder();
		sql.append("USE online_shopping_application;\n");

		// 50 vendors (role 102)
		for (int i = 1; i <= 50; i++) {
			sql.append(String.format(
					"INSERT INTO vendor (email, password, phone, vendor_name, role_id, warning_count) VALUES ('vendor%03d@example.com', '%s', '9100000%03d', 'Vendor %03d', 102, 0);%n",
					i, hash, i, i));
		}

		// 10 products owned by first 10 vendors (virtual_id = vendor id after insert: assume starting from 1 if empty)
		String[] cats = {"electronics", "menswear", "womenwear", "footwear", "home", "beauty", "sports", "books", "mobile", "watches"};
		String[] names = {"Wireless Earbuds", "Denim Jacket", "Summer Dress", "Running Shoes", "Table Lamp", "Face Cream", "Yoga Mat", "Java Guide", "Phone Case", "Smart Watch"};
		for (int i = 1; i <= 10; i++) {
			sql.append(String.format(
					"INSERT INTO product (category, image_url, price, product_description, product_name, stock_quantity, virtual_id) VALUES ('%s', 'seed-%d.png', %d, 'Seeded product %d', '%s', %d, %d);%n",
					cats[i - 1], i, 100 + (i * 25), i, names[i - 1].replace("'", "''"), 50 + i, i));
		}

		// One address per user, then 2 orders per user referencing product 1 and 2
		sql.append("""
				INSERT INTO address (address_line1, address_line2, city, name, state, virtual_id, zip_code)
				SELECT '12 Seed Street', 'Apt 1', 'Hyderabad', CONCAT('User ', id), 'TS', id, '500001'
				FROM `user`;
				""");

		sql.append("""
				INSERT INTO order_entity (order_date, total_amount, virtual_address_id, virtual_cart_id, virtual_id, virtual_product_id)
				SELECT NOW(6), 125.0, a.id, NULL, u.id, 1
				FROM `user` u
				JOIN address a ON a.virtual_id = u.id;
				""");

		sql.append("""
				INSERT INTO order_entity (order_date, total_amount, virtual_address_id, virtual_cart_id, virtual_id, virtual_product_id)
				SELECT NOW(6), 150.0, a.id, NULL, u.id, 2
				FROM `user` u
				JOIN address a ON a.virtual_id = u.id;
				""");

		Path out = Path.of("seed_vendors_products_orders.sql");
		Files.writeString(out, sql.toString());
		System.out.println("Wrote " + out.toAbsolutePath());
	}
}