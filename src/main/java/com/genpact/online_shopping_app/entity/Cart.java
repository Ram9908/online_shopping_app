package com.genpact.online_shopping_app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cart")
public class Cart {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer cartId;
	private Integer vProductId;
	private Integer quantity;
	private Integer virtualId;

	public Cart() {}

	

	public Cart(Integer vProductId, Integer virtualId, Integer quantity) {
		super();
		this.vProductId = vProductId;
		this.quantity = quantity;
		this.virtualId = virtualId;
	}



	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getVirtualId() {
		return virtualId;
	}

	public void setVirtualId(Integer virtualId) {
		this.virtualId = virtualId;
	}
	

	public Integer getvProductId() {
		return vProductId;
	}



	public void setvProductId(Integer vProductId) {
		this.vProductId = vProductId;
	}



	@Override
	public String toString() {
		return "Cart [cartId=" + cartId + ", vProductId=" + vProductId + ", quantity=" + quantity
				+ ", virtualId=" + virtualId + "]";
	}



	
	
}
