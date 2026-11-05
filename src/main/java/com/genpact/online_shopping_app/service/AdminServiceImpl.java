package com.genpact.online_shopping_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.Admin;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
    private AdminRepository adminRepository;


	@Override
	public Admin viewByEmailAndPassword(String email, String password) throws OnlineShoppingException{
		
		
		return adminRepository.findByEmailAndPassword(email, password);
		
		 
	}
	

}
