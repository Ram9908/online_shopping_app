package com.genpact.online_shopping_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.genpact.online_shopping_app.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findFirstByEmail(String email);

	boolean existsByEmail(String email);

	@Query("select u from User u where u.email = :email and u.password = :password")
	List<User> getAllUsersByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
