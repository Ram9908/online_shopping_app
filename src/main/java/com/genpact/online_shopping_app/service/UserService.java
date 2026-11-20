package com.genpact.online_shopping_app.service;

import java.util.List;
import java.util.Optional;

import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;

public interface UserService {
	User registerUser(User user) throws OnlineShoppingException; //for user registration 
	
	List<User> getAllUsers() throws OnlineShoppingException;//for admin to view all users
	
	void removeUserById(Integer userId) throws OnlineShoppingException;//for admin to delete all users

	Optional<User> findById(Integer userId) throws OnlineShoppingException;
	
	List<User> viewAllUsersByEmailAndPassword(String email, String password) throws OnlineShoppingException;
	
	User saveUser(User user) throws OnlineShoppingException;
		
}