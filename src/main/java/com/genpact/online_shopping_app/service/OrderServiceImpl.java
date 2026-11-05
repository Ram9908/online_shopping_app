package com.genpact.online_shopping_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public OrderEntity placeOrder(OrderEntity order) throws OnlineShoppingException{
		return orderRepository.save(order);
	}
	@Override
    public List<OrderEntity> getAllOrders() throws OnlineShoppingException{
        return orderRepository.findAll();
    }
	@Override
	public List<OrderEntity> viewByVirtualId(Integer virtualId) throws OnlineShoppingException{
		return orderRepository.findByVirtualId(virtualId);
	}
	@Override
	public void removeOrderById(Integer orderId) {
		orderRepository.deleteById(orderId);
		
	}
	@Override
	public Integer getOrderCountByVirtualCartId(Integer cartId) {
		// TODO Auto-generated method stub
		return orderRepository.countByVirtualCartId(cartId);
	}
	@Override
	public List<OrderEntity> viewByVirtualCartId(Integer virtualCartId) {
		// TODO Auto-generated method stub
		return orderRepository.findByVirtualCartId(virtualCartId);
	}
	@Override
	public List<OrderEntity> getOrdersByVirtualId(Integer virtualId) {
		// TODO Auto-generated method stub
		return orderRepository.findByVirtualId(virtualId);
	}

}