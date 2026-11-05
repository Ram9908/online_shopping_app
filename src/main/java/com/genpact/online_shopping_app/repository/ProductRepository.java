package com.genpact.online_shopping_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.genpact.online_shopping_app.entity.Product;

import jakarta.persistence.LockModeType;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

	List<Product> findByCategory(String category);

	List<Product> findByProductNameContainingIgnoreCase(String productName);

	List<Product> findAllByVirtualId(Integer virtualId);

	/** Row-locks the product so two concurrent checkouts cannot oversell the same stock. */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Product p WHERE p.productId = :id")
	Optional<Product> findByIdForUpdate(@Param("id") Integer id);
}
