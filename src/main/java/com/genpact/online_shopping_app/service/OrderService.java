package com.genpact.online_shopping_app.service;

import java.util.List;

import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;

public interface OrderService {

	OrderEntity placeOrder(OrderEntity order) throws OnlineShoppingException;

	List<OrderEntity> getAllOrders() throws OnlineShoppingException;
	
	List<OrderEntity> viewByVirtualId(Integer virtualId) throws OnlineShoppingException;

	void removeOrderById(Integer orderId);

	Integer getOrderCountByVirtualCartId(Integer cartId);

	List<OrderEntity> viewByVirtualCartId(Integer virtualCartId);

	List<OrderEntity> getOrdersByVirtualId(Integer virtualId);

	void removeOrdersByVirtualId(Integer virtualId);
}