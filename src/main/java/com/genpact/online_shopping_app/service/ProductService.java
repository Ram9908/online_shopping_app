package com.genpact.online_shopping_app.service;

import java.util.List;
import java.util.Optional;

import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;


public interface ProductService {

	Product addNewProduct(Product product) throws OnlineShoppingException;
	List<Product> viewAllProducts() throws OnlineShoppingException;
	void deleteProductById(Integer productId) throws OnlineShoppingException;
	
	List<Product> searchProductByCategory(String category) throws OnlineShoppingException;

	Optional<Product> viewByProductId(Integer productId) throws OnlineShoppingException;
	
	List<Product> searchByProductName(String productName) throws OnlineShoppingException;
	
	List<Product> viewAllByVirtualId(Integer virtualId) throws OnlineShoppingException;

	List<String> listCategories() throws OnlineShoppingException;

}
