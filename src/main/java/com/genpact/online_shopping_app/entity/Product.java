package com.genpact.online_shopping_app.entity;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity

public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer productId;
	private String category;
	private String productName;
	private String productDescription;
	private Integer price;
	private Integer stockQuantity;
	private Integer virtualId;
	private String imageUrl;

	public Product() {}

	public Product(String category, String productName, String productDescription, Integer price, Integer stockQuantity,
			Integer virtualId, String imageUrl) {
		super();
		this.category = category;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.virtualId = virtualId;
		this.imageUrl = imageUrl;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getVirtualId() {
		return virtualId;
	}

	public void setVirtualId(Integer virtualId) {
		this.virtualId = virtualId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	@Override
	public String toString() {
		return "Product [category=" + category + ", productName=" + productName + ", productDescription="
				+ productDescription + ", price=" + price + ", stockQuantity=" + stockQuantity + ", virtualId="
				+ virtualId + ", imageUrl=" + imageUrl + "]";
	}

}
