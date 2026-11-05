package com.genpact.online_shopping_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genpact.online_shopping_app.entity.OrderEntity;
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
	List<OrderEntity> findByVirtualId(Integer virtualId);

	Integer countByVirtualCartId(Integer cartId);
	
	List<OrderEntity> findByVirtualCartId(Integer virtualCartId);
}