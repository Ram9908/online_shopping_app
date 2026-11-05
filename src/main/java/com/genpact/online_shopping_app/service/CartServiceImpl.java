package com.genpact.online_shopping_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;

	@Override
	public Cart addToCart(Cart cart) throws OnlineShoppingException{	
		return cartRepository.save(cart);
	}

	@Override
	public List<Cart> viewAllByVirtualId(Integer virtualId) throws OnlineShoppingException{		
		return cartRepository.findAllByVirtualId(virtualId);
	}
	
	@Override
    public Cart getCartById(Integer cartId) throws OnlineShoppingException{
        return cartRepository.findById(cartId).orElse(null);
    }

	@Override
	public void deleteCartById(Integer cartId) throws OnlineShoppingException{
		cartRepository.deleteById(cartId);
		
	}

	@Override
	public List<Cart> viewAllCarts() throws OnlineShoppingException{
		
		return (List<Cart>) cartRepository.findAll();
	}

	@Override
	public Cart updateQuantity(Cart cart) {
		// TODO Auto-generated method stub
		return cartRepository.save(cart);
	}
	

}
