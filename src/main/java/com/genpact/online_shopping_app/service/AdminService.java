package com.genpact.online_shopping_app.service;



import com.genpact.online_shopping_app.entity.Admin;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;


public interface AdminService  {

	Admin viewByEmailAndPassword(String email,String password) throws OnlineShoppingException;
}
