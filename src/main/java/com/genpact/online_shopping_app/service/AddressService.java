package com.genpact.online_shopping_app.service;

import java.util.List;

import com.genpact.online_shopping_app.entity.Address;

public interface AddressService {
	Address addAddress(Address address);

	List<Address> viewAllByVirtualId(Integer virtualId);

	List<Address> viewAllAddress();

	void deleteByVirtualId(Integer virtualId);
}
