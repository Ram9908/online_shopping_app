package com.genpact.online_shopping_app.service;

import java.util.List;
import java.util.Optional;

import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;

public interface VendorService {
	  
	Vendor register(Vendor vendor) throws OnlineShoppingException;
	Vendor viewByEmailAndPassword(String email,String password) throws OnlineShoppingException;
	List<Vendor> getAllVendors() throws OnlineShoppingException;
	Optional<Vendor> findById(Integer vendorId) throws OnlineShoppingException;
	Vendor save(Vendor vendor) throws OnlineShoppingException;
	void removeVendorById(Integer vendorId) throws OnlineShoppingException;
}
