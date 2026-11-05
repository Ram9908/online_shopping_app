package com.genpact.online_shopping_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	

	@Override
	public Product addNewProduct(Product product) throws OnlineShoppingException{
		return productRepository.save(product);		
	}

	@Override
	public List<Product> viewAllProducts() throws OnlineShoppingException{		
		return (List<Product>) productRepository.findAll();
	}

	@Override
	public void deleteProductById(Integer productId) throws OnlineShoppingException{
		productRepository.deleteById(productId);
	}

	@Override
	public List<Product> searchProductByCategory(String category) throws OnlineShoppingException{		
		return productRepository.findByCategory(category);
	}

	@Override
	public Optional<Product> viewByProductId(Integer productId) throws OnlineShoppingException{		
		return productRepository.findById(productId);
	}
	
	@Override
    public List<Product> searchByProductName(String productName) throws OnlineShoppingException{
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

	@Override
	public List<Product> viewAllByVirtualId(Integer virtualId) throws OnlineShoppingException{
		return productRepository.findAllByVirtualId(virtualId);
	}

}
