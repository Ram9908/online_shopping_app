package com.genpact.online_shopping_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {
	@Autowired
	private VendorRepository vendorRepository;
	
	@Override
	public Vendor register(Vendor vendor) throws OnlineShoppingException{
		return vendorRepository.save(vendor) ;
	}

	@Override
	public Vendor viewByEmailAndPassword(String email, String password) throws OnlineShoppingException{		
		return vendorRepository.findByEmailAndPassword(email, password) ;
	}
	@Override
	public List<Vendor> getAllVendors() throws OnlineShoppingException{
		return (List<Vendor>) vendorRepository.findAll();
	}

	@Override
	public Optional<Vendor> findById(Integer vendorId) throws OnlineShoppingException {
		return vendorRepository.findById(vendorId);
	}

	@Override
	public Vendor save(Vendor vendor) throws OnlineShoppingException {
		return vendorRepository.save(vendor);
	}

	@Override
	public void removeVendorById(Integer vendorId) throws OnlineShoppingException{
		vendorRepository.deleteById(vendorId);
	}

}
