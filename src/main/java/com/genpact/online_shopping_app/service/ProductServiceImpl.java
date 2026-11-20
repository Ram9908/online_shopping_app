package com.genpact.online_shopping_app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;

	private static final List<String> STANDARD_CATEGORIES = List.of(
			"womenwear", "menswear", "kidswear", "watches", "electronics",
			"footwear", "home", "beauty", "sports", "books", "mobile");

	private static final Set<String> STANDARD_CATEGORY_SET = new HashSet<>(STANDARD_CATEGORIES);

	public static boolean isStandardCategory(String category) {
		if (category == null || category.trim().isEmpty()) {
			return false;
		}
		String cleaned = category.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
		return STANDARD_CATEGORY_SET.contains(cleaned);
	}

	private static String normalizeCategory(String raw) {
		if (raw == null) {
			return null;
		}
		String cleaned = raw.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
		return cleaned.isEmpty() ? null : cleaned;
	}

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
		String selected = normalizeCategory(category);
		if ("other".equals(selected)) {
			List<Product> otherProducts = new ArrayList<>();
			for (Product product : viewAllProducts()) {
				if (!isStandardCategory(product.getCategory())) {
					otherProducts.add(product);
				}
			}
			return otherProducts;
		}
		if (selected == null) {
			return List.of();
		}
		return productRepository.findByCategory(selected);
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

	@Override
	public List<String> listCategories() throws OnlineShoppingException {
		List<String> categories = new ArrayList<>(STANDARD_CATEGORIES);
		Collections.sort(categories);
		categories.add("other");
		return categories;
	}

}
