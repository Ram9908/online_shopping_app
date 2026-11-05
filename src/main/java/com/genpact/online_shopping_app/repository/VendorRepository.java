package com.genpact.online_shopping_app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.genpact.online_shopping_app.entity.Vendor;

@Repository
public interface VendorRepository extends CrudRepository<Vendor, Integer> {

	Vendor findByEmailAndPassword(String email, String password);

	Optional<Vendor> findFirstByEmail(String email);

	boolean existsByEmail(String email);
}
