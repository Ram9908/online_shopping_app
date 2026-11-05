package com.genpact.online_shopping_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.genpact.online_shopping_app.entity.Cart;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

	List<Cart> findAllByVirtualId(Integer virtualId);

	@Query("SELECT c FROM Cart c WHERE c.cartId = :cartId")
	Cart findCartBycartId(@Param("cartId") Integer cartId);
}
