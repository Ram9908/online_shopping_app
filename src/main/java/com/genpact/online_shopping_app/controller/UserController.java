package com.genpact.online_shopping_app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.entity.Address;
import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.service.AddressService;
import com.genpact.online_shopping_app.service.CartService;
import com.genpact.online_shopping_app.service.OrderService;
import com.genpact.online_shopping_app.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AddressService addressService;

	private static ModelAndView loginRedirect() {
		return new ModelAndView("redirect:/login.jsp");
	}

	private static ModelAndView error(String message) {
		ModelAndView mv = new ModelAndView("errorPage_view");
		mv.addObject("error", message);
		return mv;
	}

	@GetMapping("view/all")
	public ModelAndView viewAllProducts() {
		ModelAndView mv = new ModelAndView();
		try {
			List<Product> inStock = new ArrayList<>();
			for (Product product : productService.viewAllProducts()) {
				if (product.getStockQuantity() > 0) {
					inStock.add(product);
				}
			}
			mv.setViewName("all_products_user_view");
			mv.addObject("productList", inStock);
		} catch (OnlineShoppingException e) {
			return error("An error occurred while fetching products: " + e.getMessage());
		}
		return mv;
	}

	/** Adds to the cart only. Stock is checked here but is reduced at checkout, not now. */
	@GetMapping("view/add/cart/{productId}")
	public ModelAndView addToCart(@PathVariable("productId") Integer productId, @RequestParam Integer quantity,
			@RequestParam(value = "userId", required = false) Integer requestedUserId, HttpSession session) {
		User activeUser = (User) session.getAttribute("activeUser");
		if (activeUser == null) {
			return loginRedirect();
		}
		try {
			Product product = productService.viewByProductId(productId).orElse(null);
			if (product == null) {
				return error("Product not found.");
			}
			if (quantity == null || quantity < 1) {
				return error("Quantity must be at least 1.");
			}
			Integer userId = activeUser.getUserId(); // always the logged-in user, never a request parameter
			Cart existing = null;
			for (Cart c : cartService.viewAllByVirtualId(userId)) {
				if (c.getvProductId().equals(productId)) {
					existing = c;
					break;
				}
			}
			int newQuantity = quantity + (existing == null ? 0 : existing.getQuantity());
			if (newQuantity > product.getStockQuantity()) {
				return error("Only " + product.getStockQuantity() + " of '" + product.getProductName()
						+ "' in stock.");
			}
			if (existing != null) {
				existing.setQuantity(newQuantity);
				cartService.addToCart(existing);
			} else {
				cartService.addToCart(new Cart(productId, userId, quantity));
			}
			ModelAndView mv = new ModelAndView("addToCart_view");
			mv.addObject("userCart", "Cart Added Successfully");
			return mv;
		} catch (OnlineShoppingException e) {
			return error("An error occurred while adding to cart: " + e.getMessage());
		}
	}

	@GetMapping("view/all/cart")
	public ModelAndView viewAllCartItems(HttpSession session) {
		User activeUser = (User) session.getAttribute("activeUser");
		if (activeUser == null) {
			return loginRedirect();
		}
		try {
			List<Product> productList = productService.viewAllProducts();
			List<Cart> cartItems = cartService.viewAllByVirtualId(activeUser.getUserId());
			long total = 0L;
			for (Cart item : cartItems) {
				for (Product product : productList) {
					if (product.getProductId().equals(item.getvProductId())) {
						total += (long) product.getPrice() * item.getQuantity();
					}
				}
			}
			ModelAndView mv = new ModelAndView("cart_view");
			mv.addObject("cartItems", cartItems);
			mv.addObject("productList", productList);
			mv.addObject("totalQuantity", total);
			return mv;
		} catch (OnlineShoppingException e) {
			return error("An error occurred while fetching cart items: " + e.getMessage());
		}
	}

	@GetMapping("view/all/delete/{cartId}")
	public ModelAndView deleteCart(@PathVariable Integer cartId, HttpSession session) {
		User activeUser = (User) session.getAttribute("activeUser");
		if (activeUser == null) {
			return loginRedirect();
		}
		try {
			for (Cart cart : cartService.viewAllByVirtualId(activeUser.getUserId())) {
				if (cart.getCartId().equals(cartId)) {
					cartService.deleteCartById(cartId);
					ModelAndView mv = new ModelAndView("delete_message_view");
					mv.addObject("deleted", "Cart deleted successfully");
					return mv;
				}
			}
			return error("Cart item not found.");
		} catch (OnlineShoppingException e) {
			return error("An error occurred while deleting the cart item: " + e.getMessage());
		}
	}

	@GetMapping("view/all/update/{cartId}")
	public ModelAndView updateCart(@PathVariable Integer cartId, @RequestParam Integer quantity,
			HttpSession session) {
		User activeUser = (User) session.getAttribute("activeUser");
		if (activeUser == null) {
			return loginRedirect();
		}
		try {
			for (Cart cart : cartService.viewAllByVirtualId(activeUser.getUserId())) {
				if (cart.getCartId().equals(cartId)) {
					Product product = productService.viewByProductId(cart.getvProductId()).orElse(null);
					if (product == null) {
						return error("Product is no longer available.");
					}
					if (quantity == null || quantity < 1 || quantity > product.getStockQuantity()) {
						return error("Quantity must be between 1 and " + product.getStockQuantity() + ".");
					}
					cart.setQuantity(quantity);
					cartService.addToCart(cart);
					ModelAndView mv = new ModelAndView("delete_message_view");
					mv.addObject("updated", "Cart Updated successfully");
					return mv;
				}
			}
			return error("Cart item not found.");
		} catch (OnlineShoppingException e) {
			return error("An error occurred while updating the cart: " + e.getMessage());
		}
	}

	@GetMapping("/ordersView")
	public ModelAndView viewAllOrders(HttpSession session) {
		User activeUser = (User) session.getAttribute("activeUser");
		if (activeUser == null) {
			return loginRedirect();
		}
		try {
			Integer userId = activeUser.getUserId();
			List<OrderEntity> orders = orderService.getOrdersByVirtualId(userId);
			List<Product> productList = productService.viewAllProducts();
			List<Address> addressList = addressService.viewAllByVirtualId(userId);
			ModelAndView mv = new ModelAndView("order_details_user");
			mv.addObject("productList", productList);
			mv.addObject("updatedOrder", orders);
			mv.addObject("addressList", addressList);
			return mv;
		} catch (OnlineShoppingException e) {
			return error("Failed to fetch order list: " + e.getMessage());
		}
	}

	@GetMapping("/products/category/{category}")
	public ModelAndView searchProductsByCategory(@PathVariable String category) {
		try {
			ModelAndView mv = new ModelAndView("all_products_user_view");
			mv.addObject("productList", productService.searchProductByCategory(category));
			return mv;
		} catch (OnlineShoppingException e) {
			return error("An error occurred while searching products by category: " + e.getMessage());
		}
	}

	@GetMapping("/productsearch/{productName}")
	public ModelAndView searchProductByName(@RequestParam String productName) {
		try {
			ModelAndView mv = new ModelAndView("all_products_user_view");
			mv.addObject("productList", productService.searchByProductName(productName));
			return mv;
		} catch (OnlineShoppingException e) {
			return error("An error occurred while searching products: " + e.getMessage());
		}
	}
}
