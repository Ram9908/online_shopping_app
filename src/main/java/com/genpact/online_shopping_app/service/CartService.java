package com.genpact.online_shopping_app.service;

import java.util.List;

import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;

public interface CartService {
	
	Cart addToCart(Cart cart) throws OnlineShoppingException;
	List<Cart> viewAllByVirtualId(Integer virtualId) throws OnlineShoppingException;
	Cart getCartById(Integer cartId) throws OnlineShoppingException;
	void deleteCartById(Integer cartId) throws OnlineShoppingException;
	List<Cart> viewAllCarts() throws OnlineShoppingException;
	Cart updateQuantity(Cart cart);
}
