package com.genpact.online_shopping_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.genpact.online_shopping_app.entity.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin,Integer>{
	
	Admin findByEmailAndPassword(String email,String password);

}
