package com.genpact.online_shopping_app.service;

import java.util.List;

import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;

public interface VendorService {
	  
	Vendor register(Vendor vendor) throws OnlineShoppingException;
	Vendor viewByEmailAndPassword(String email,String password) throws OnlineShoppingException;
	List<Vendor> getAllVendors() throws OnlineShoppingException;
	void removeVendorById(Integer vendorId) throws OnlineShoppingException;
}
