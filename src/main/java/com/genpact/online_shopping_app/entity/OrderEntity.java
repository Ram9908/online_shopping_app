package com.genpact.online_shopping_app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "cart_id", unique=true)
//    private Cart cart;
    
    private LocalDateTime orderDate;
    
    private Float totalAmount;
    
    private Integer virtualId;
    private Integer virtualCartId;
    private Integer virtualProductId;
    private Integer virtualAddressId;
    public OrderEntity() {}

	

	public OrderEntity(LocalDateTime orderDate, Float totalAmount, Integer virtualId, Integer virtualCartId,
			Integer virtualProductId, Integer virtualAddressId) {
		super();
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.virtualId = virtualId;
		this.virtualCartId = virtualCartId;
		this.virtualProductId = virtualProductId;
		this.virtualAddressId = virtualAddressId;
	}



	public Integer getVirtualAddressId() {
		return virtualAddressId;
	}



	public void setVirtualAddressId(Integer virtualAddressId) {
		this.virtualAddressId = virtualAddressId;
	}



	public Integer getVirtualProductId() {
		return virtualProductId;
	}







	public void setVirtualProductId(Integer virtualProductId) {
		this.virtualProductId = virtualProductId;
	}







	public Integer getVirtualCartId() {
		return virtualCartId;
	}



	public void setVirtualCartId(Integer virtualCartId) {
		this.virtualCartId = virtualCartId;
	}



	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}



	public Integer getVirtualId() {
		return virtualId;
	}

	public void setVirtualId(Integer virtualId) {
		this.virtualId = virtualId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}



	@Override
	public String toString() {
		return "OrderEntity [orderId=" + orderId + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount
				+ ", virtualId=" + virtualId + ", virtualCartId=" + virtualCartId + ", virtualProductId="
				+ virtualProductId + ", virtualAddressId=" + virtualAddressId + "]";
	}


}
