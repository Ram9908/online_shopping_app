package com.genpact.online_shopping_app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.entity.Address;
import com.genpact.online_shopping_app.entity.Cart;
import com.genpact.online_shopping_app.entity.OrderEntity;
import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.service.AddressService;
import com.genpact.online_shopping_app.service.AdminService;
import com.genpact.online_shopping_app.service.CartService;
import com.genpact.online_shopping_app.service.OrderService;
import com.genpact.online_shopping_app.service.ProductService;
import com.genpact.online_shopping_app.service.UserService;
import com.genpact.online_shopping_app.service.VendorService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
    @Autowired
    private AdminService serviceLogin;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressService addressService;
    

    @GetMapping("/admin")
    public String adminHome(HttpSession session) {
        if (session.getAttribute("activeAdmin") == null) {
            return "redirect:/login.jsp";
        }
        return "admin_index";
    }

    @GetMapping("/add/vendor")
    public ModelAndView vendorsList() {
        ModelAndView mv = new ModelAndView();
        try {
            List<Vendor> vendorList = vendorService.getAllVendors();
            mv.setViewName("admin_vendors_view");
            mv.addObject("vendorList", vendorList);
        } catch (OnlineShoppingException e) {
            mv.addObject("error", "Failed to fetch vendor list: " + e.getMessage());
            mv.setViewName("errorPage_view");
        }
        return mv;
    }

    @GetMapping("/add/user")
    public ModelAndView usersList() {
        ModelAndView mv = new ModelAndView();
        try {
            List<User> userList = userService.getAllUsers();
            mv.setViewName("admin_users_view");
            mv.addObject("userList", userList);
        } catch (OnlineShoppingException e) {
            mv.addObject("error", "Failed to fetch user list: " + e.getMessage());
            mv.setViewName("errorPage_view");
        }
        return mv;
    }

    
    @GetMapping("/orders")
    public ModelAndView viewAllOrders(ModelAndView mv) {
    	
        try {
        	
        	List<OrderEntity> updatedOrder = orderService.getAllOrders();
            List<Product> productList = productService.viewAllProducts();
            List<Address> addressList=addressService.viewAllAddress();
            List<Cart> cartItems =new ArrayList<>();
            Cart cart=null;
            for(OrderEntity order:updatedOrder) {            	
            cart=cartService.getCartById(order.getVirtualCartId());
            cartItems.add(cart);            
            }
            mv.addObject("cartItems", cartItems);
            mv.addObject("productList", productList);
            mv.addObject("updatedOrder", updatedOrder);
            mv.addObject("addressList", addressList);
            mv.setViewName("order_details_admin"); 
              
             
        } catch (OnlineShoppingException e) {
            mv.addObject("error", "Failed to fetch order list: " + e.getMessage());
            mv.setViewName("errorPage_view");
        }
        return mv;
    }
    @GetMapping("view/product/admin")
    public ModelAndView viewAllProducts() {            // user can view all the produts.
        ModelAndView mv = new ModelAndView();
        try {
            mv.setViewName("admin_product_view");
            List<Product> productList = productService.viewAllProducts();
            mv.addObject("productList", productList);
        } catch (OnlineShoppingException e) {
            mv.addObject("error", "An error occurred while fetching products: " + e.getMessage());
            mv.setViewName("errorPage_view");
        }
        return mv;
    }
}
