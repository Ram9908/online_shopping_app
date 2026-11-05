package com.genpact.online_shopping_app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.service.ProductService;
import com.genpact.online_shopping_app.service.VendorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class VendorController {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;
    @Autowired
    ProductService productService;
    @Autowired
    VendorService vendorService;
    private List<Product> productList = new LinkedList<>();

    @PostMapping("/product")
    public ModelAndView addNewProduct(
            HttpSession session,HttpServletRequest request) throws IOException, ServletException {
        ModelAndView mv = new ModelAndView();
        try {
        	
			String productName=request.getParameter("productName");
			String productDescription=request.getParameter("productDescription");
			Integer price=Integer.parseInt(request.getParameter("price"));
			Integer stockQuantity=Integer.parseInt(request.getParameter("stockQuantity"));
			String category=request.getParameter("category");
			Part part=request.getPart("image");
    		
    		String fileName = null;
    		if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null) {
    			fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
    			Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
    			Files.createDirectories(dir);
    			Path target = dir.resolve(fileName).normalize();
    			if (!target.startsWith(dir)) {
    				throw new IOException("Invalid file name");
    			}
    			try (InputStream is = part.getInputStream()) {
    				Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
    			}
    		}
    		
            Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
            	Integer vVendorId=activeVendor.getVendorId();
                Product product = new Product(category, productName, productDescription, price, stockQuantity,vVendorId,fileName);
                productList.add(product);
                productService.addNewProduct(product);
                
                mv.addObject("product", "product added successfully");
                mv.setViewName("product_details_view");
            }
        } catch (OnlineShoppingException e) {
            mv.addObject("error", "An error occurred while adding new product: " + e.getMessage());
            mv.setViewName("errorPage_view");
        }
        return mv;
    }

    @GetMapping("/view/all/products")
    public ModelAndView viewAllProducts(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        try {
        	Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
            Integer vVendorId=activeVendor.getVendorId();
            List<Product> productItems = productService.viewAllByVirtualId(vVendorId);          
            mv.addObject("productItems", productItems);
            mv.setViewName("product_view");
            }
        } catch (OnlineShoppingException e) {
            mv.addObject("error", "An error occurred while fetching all products: " + e.getMessage());
            mv.setViewName("errorPage_view");
        }
        return mv;
    }

    @GetMapping("view/all/remove/product/{productId}")
    public ModelAndView deleteProduct(@PathVariable Integer productId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            boolean found = false;
            Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
                List<Product> productList = productService.viewAllProducts();
                for (Product product : productList) {
                    if (product.getProductId().equals(productId)) {
                        found = true;
                        productService.deleteProductById(productId);
                        modelAndView.addObject("deleted", "Product deleted successfully");
                        modelAndView.setViewName("delete_message_view");
                        break;
                    }
                    if (!found) {
                        modelAndView.addObject("error", "Failed to delete product: " + productId);
                        modelAndView.setViewName("errorPage_deleted_view");
                    }
                }
            }
        } catch (OnlineShoppingException e) {
            modelAndView.addObject("error", "An error occurred while deleting product: " + e.getMessage());
            modelAndView.setViewName("errorPage_view");
        }
        return modelAndView;
    }

    

    @GetMapping("view/all/update/product/{productId}")
    public ModelAndView editProduct(@PathVariable Integer productId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            boolean found = false;
            Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
                List<Product> productList = productService.viewAllProducts();
                for (Product product : productList) {
                    if (product.getProductId().equals(productId)) {
                        found = true;     
                        modelAndView.setViewName("update_product");
                        modelAndView.addObject("product", product);
                        modelAndView.addObject("productList", productList);
                    }                   
                }
            }
            if (!found) {
                modelAndView.addObject("error", "Product not found with id: " + productId);
                modelAndView.setViewName("errorPage_view");
            }
        } catch (OnlineShoppingException e) {
            modelAndView.addObject("error", "An error occurred while editing product: " + e.getMessage());
            modelAndView.setViewName("errorPage_view");
        }
        return modelAndView;
    }

    @PostMapping("view/all/update/product/product/update")
    public ModelAndView updateProduct(@RequestParam Integer productId,
            @RequestParam String category,
            @RequestParam String productName,
            @RequestParam String productDescription,
            @RequestParam Integer price,
            @RequestParam Integer stockQuantity, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
                Optional<Product> optional = productService.viewByProductId(productId);
                if (optional.isPresent()) {
                    Product product = optional.get();
                    product.setProductId(productId);
                    product.setCategory(category);
                    product.setProductName(productName);
                    product.setProductDescription(productDescription);
                    product.setPrice(price);
                    product.setStockQuantity(stockQuantity);
                    productService.addNewProduct(product);
                    modelAndView.setViewName("success");
                    modelAndView.addObject("successMessage", "Product updated successfully ");
                }
            }
        } catch (OnlineShoppingException e) {
            modelAndView.addObject("error", "An error occurred while updating product: " + e.getMessage());
            modelAndView.setViewName("errorPage_view");
        }
        return modelAndView;
    }
    
    @GetMapping("/p/category/{category}")
    public ModelAndView searchProductsByCategory(@PathVariable String category,HttpSession session) {
        ModelAndView mv = new ModelAndView();
        try {
        	Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
            	Integer vVendorId=activeVendor.getVendorId();
            	List<Product> productItems = productService.viewAllByVirtualId(vVendorId);
            	productList.clear();
            	for(Product product:productItems) {
            		if(product.getCategory().equals(category)) {
            			productList.add(product);
            		}
            	}
            mv.addObject("productItems", productList);
            mv.setViewName("product_view");
            }
        } catch (OnlineShoppingException e) {
            mv.addObject("error", "An error occurred while searching products by category: " + e.getMessage());
            mv.setViewName("errorPage_view");
        }
        return mv;
    }
    @GetMapping("/p/{productName}")
    public ModelAndView searchProductByName(@RequestParam String productName,HttpSession session) {
        ModelAndView mv = new ModelAndView();
        try {
        	Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
            	Integer vVendorId=activeVendor.getVendorId();
            	List<Product> productItems = productService.viewAllByVirtualId(vVendorId);
            	productList.clear();
            	for(Product product:productItems) {
            		if(product.getProductName().equals(productName)) {
            			productList.add(product);
            		}
            	}
	            mv.addObject("productItems", productList);
	            mv.setViewName("product_view");
            }
        }catch (OnlineShoppingException e) {
            mv.addObject("error", "An error occurred while fetching orders: ");
            mv.setViewName("errorPage_view");
        }
        return mv;        
    }
}
