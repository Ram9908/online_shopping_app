package com.genpact.online_shopping_app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genpact.online_shopping_app.entity.Address;
import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.repository.AddressRepository;
import com.genpact.online_shopping_app.repository.CartRepository;
import com.genpact.online_shopping_app.repository.OrderRepository;
import com.genpact.online_shopping_app.repository.ProductRepository;

/**
 * Turns a user's cart into orders in ONE transaction: stock is checked and decremented
 * here (not when adding to the cart), so a failure leaves no partial orders behind.
 */
@Service
public class CheckoutService {

	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final AddressRepository addressRepository;

	public CheckoutService(CartRepository cartRepository, ProductRepository productRepository,
			OrderRepository orderRepository, AddressRepository addressRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.addressRepository = addressRepository;
	}

	@Transactional
	public List<OrderEntity> checkout(Integer userId, Address address) {
		List<Cart> carts = cartRepository.findAllByVirtualId(userId);
		if (carts.isEmpty()) {
			throw new IllegalStateException("Your cart is empty.");
		}
		Address savedAddress = addressRepository.save(address);

		List<OrderEntity> orders = new ArrayList<>();
		for (Cart cart : carts) {
			Product product = productRepository.findByIdForUpdate(cart.getvProductId())
					.orElseThrow(() -> new IllegalStateException("A product in your cart is no longer available."));
			int qty = cart.getQuantity();
			if (product.getStockQuantity() < qty) {
				throw new IllegalStateException("Only " + product.getStockQuantity() + " left of '"
						+ product.getProductName() + "'. Please update your cart.");
			}
			product.setStockQuantity(product.getStockQuantity() - qty);
			productRepository.save(product);

			OrderEntity order = new OrderEntity(LocalDateTime.now(), (float) (product.getPrice() * qty), userId,
					cart.getCartId(), product.getProductId(), savedAddress.getId());
			orders.add(orderRepository.save(order));
			cartRepository.delete(cart);
		}
		return orders;
	}
}
