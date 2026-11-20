package com.genpact.online_shopping_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.Address;
import com.genpact.online_shopping_app.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	private AddressRepository addressRepository;
	@Override
	public Address addAddress(Address address) {
		return addressRepository.save(address);
	}
	@Override
	public List<Address> viewAllByVirtualId(Integer virtualId) {
		// TODO Auto-generated method stub
		return addressRepository.findByVirtualId(virtualId);
	}
	@Override
	public List<Address> viewAllAddress() {
		// TODO Auto-generated method stub
		return addressRepository.findAll();
	}

	@Override
	public void deleteByVirtualId(Integer virtualId) {
		List<Address> addresses = addressRepository.findByVirtualId(virtualId);
		if (addresses != null && !addresses.isEmpty()) {
			addressRepository.deleteAll(addresses);
		}
	}

}
