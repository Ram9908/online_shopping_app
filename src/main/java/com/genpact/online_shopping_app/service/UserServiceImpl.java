package com.genpact.online_shopping_app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.UserRepository;
import com.genpact.online_shopping_app.repository.VendorRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public User registerUser(User user) throws OnlineShoppingException{		
		return userRepository.save(user);
	}
	
	@Override
	public List<User> viewAllUsersByEmailAndPassword(String email, String password) throws OnlineShoppingException{		
		return userRepository.getAllUsersByEmailAndPassword(email, password);
	}

	@Override
	public List<User> getAllUsers() throws OnlineShoppingException{		
		return (List<User>) userRepository.findAll();
	}

	@Override
	public void removeUserById(Integer userId) throws OnlineShoppingException {		
		userRepository.deleteById(userId);		
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

}