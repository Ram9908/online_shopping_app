package com.genpact.online_shopping_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.genpact.online_shopping_app.entity.Address;
import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.repository.AddressRepository;
import com.genpact.online_shopping_app.repository.CartRepository;
import com.genpact.online_shopping_app.repository.OrderRepository;
import com.genpact.online_shopping_app.repository.ProductRepository;
import com.genpact.online_shopping_app.service.CheckoutService;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

	@Mock
	private CartRepository cartRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private AddressRepository addressRepository;

	private CheckoutService checkoutService;
	private Address address;
	private Address savedAddress;

	@BeforeEach
	void setUp() {
		checkoutService = new CheckoutService(cartRepository, productRepository, orderRepository, addressRepository);
		address = new Address("Asha", "12 Main St", "", "Hyderabad", "TS", "500001", 1);
		savedAddress = new Address("Asha", "12 Main St", "", "Hyderabad", "TS", "500001", 1);
		savedAddress.setId(7);
	}

	private Product product(int id, int price, int stock) {
		Product p = new Product("Stationery", "Pen", "Blue pen", price, stock, 99, "pen.png");
		p.setProductId(id);
		return p;
	}

	private Cart cart(int cartId, int productId, int qty) {
		Cart c = new Cart(productId, 1, qty);
		c.setCartId(cartId);
		return c;
	}

	@Test
	void checkout_createsOrder_reducesStock_andClearsCart() {
		Cart cart = cart(5, 10, 2);
		Product pen = product(10, 50, 10);
		when(cartRepository.findAllByVirtualId(1)).thenReturn(List.of(cart));
		when(addressRepository.save(address)).thenReturn(savedAddress);
		when(productRepository.findByIdForUpdate(10)).thenReturn(Optional.of(pen));
		when(orderRepository.save(any(OrderEntity.class))).thenAnswer(inv -> inv.getArgument(0));

		List<OrderEntity> orders = checkoutService.checkout(1, address);

		assertEquals(1, orders.size());
		assertEquals(100f, orders.get(0).getTotalAmount());
		assertEquals(7, orders.get(0).getVirtualAddressId());
		assertEquals(8, pen.getStockQuantity());
		verify(cartRepository).delete(cart);
	}

	@Test
	void checkout_insufficientStock_throwsAndCreatesNoOrder() {
		Cart cart = cart(5, 10, 3);
		Product pen = product(10, 50, 2);
		when(cartRepository.findAllByVirtualId(1)).thenReturn(List.of(cart));
		when(addressRepository.save(address)).thenReturn(savedAddress);
		when(productRepository.findByIdForUpdate(10)).thenReturn(Optional.of(pen));

		assertThrows(IllegalStateException.class, () -> checkoutService.checkout(1, address));

		assertEquals(2, pen.getStockQuantity());
		verify(orderRepository, never()).save(any(OrderEntity.class));
		verify(cartRepository, never()).delete(any(Cart.class));
	}

	@Test
	void checkout_emptyCart_throws() {
		when(cartRepository.findAllByVirtualId(1)).thenReturn(List.of());

		assertThrows(IllegalStateException.class, () -> checkoutService.checkout(1, address));

		verify(addressRepository, never()).save(any(Address.class));
	}

	@Test
	void checkout_productRemoved_throws() {
		when(cartRepository.findAllByVirtualId(1)).thenReturn(List.of(cart(5, 10, 1)));
		when(addressRepository.save(address)).thenReturn(savedAddress);
		when(productRepository.findByIdForUpdate(10)).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, () -> checkoutService.checkout(1, address));
	}
}
