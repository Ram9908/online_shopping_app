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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.genpact.online_shopping_app.entity.Product;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.service.ProductService;
import com.genpact.online_shopping_app.service.ProductServiceImpl;
import com.genpact.online_shopping_app.service.VendorService;
import com.genpact.online_shopping_app.util.FlashMessages;

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

    @GetMapping("/vendor")
    public ModelAndView vendorHome(HttpSession session) {
        if (session.getAttribute("activeVendor") == null) {
            return new ModelAndView("redirect:/login.jsp");
        }
        ModelAndView mv = new ModelAndView("vendor_index");
        try {
            mv.addObject("categories", productService.listCategories());
        } catch (OnlineShoppingException e) {
            mv.addObject("categories", List.of());
        }
        return mv;
    }

    /** Opening /product in the browser (or via Back/Forward) must never re-run the POST. */
    @GetMapping("/product")
    public String productFormGet() {
        return "redirect:/product_details.jsp";
    }

    @GetMapping("/product/success")
    public String productAddedSuccess(HttpSession session) {
        if (session.getAttribute("activeVendor") == null) {
            return "redirect:/login.jsp";
        }
        FlashMessages.success(session, "Product added successfully!");
        return "redirect:/product_details.jsp";
    }

    @PostMapping("/product")
    public ModelAndView addNewProduct(
            HttpSession session,HttpServletRequest request) throws IOException, ServletException {
        ModelAndView mv = new ModelAndView();
        try {
        	
			String productName=request.getParameter("productName");
			String productDescription=request.getParameter("productDescription");
			Integer price=Integer.parseInt(request.getParameter("price"));
			Integer stockQuantity=Integer.parseInt(request.getParameter("stockQuantity"));
			String category = normalizeCategory(request.getParameter("category"));
			if (category == null || category.isEmpty()) {
				FlashMessages.error(session, "Category is required.");
				mv.setViewName("redirect:/product_details.jsp");
				return mv;
			}
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
                FlashMessages.success(session, "Product added successfully!");
                RedirectView redirect = new RedirectView("/product_details.jsp", true);
                redirect.setStatusCode(HttpStatus.SEE_OTHER);
                mv.setView(redirect);
            } else {
                FlashMessages.error(session, "Please login as a vendor.");
                mv.setViewName("redirect:/login.jsp");
            }
        } catch (OnlineShoppingException e) {
            FlashMessages.error(session, "An error occurred while adding new product: " + e.getMessage());
            mv.setViewName("redirect:/product_details.jsp");
        } catch (RuntimeException e) {
            FlashMessages.error(session, "An error occurred while adding new product: " + e.getMessage());
            mv.setViewName("redirect:/product_details.jsp");
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
            mv.addObject("categories", productService.listCategories());
            mv.addObject("selectedCategory", "all");
            mv.setViewName("product_view");
            }
        } catch (OnlineShoppingException e) {
            FlashMessages.error(session, "An error occurred while fetching all products: " + e.getMessage());
            mv.setViewName("redirect:/vendor");
        }
        return mv;
    }

    @GetMapping("view/all/remove/product/{productId}")
    public ModelAndView removeProductStock(@PathVariable Integer productId,
            @RequestParam Integer quantity, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("redirect:/view/all/products");
        try {
            Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor == null) {
                FlashMessages.error(session, "Please login as a vendor.");
                modelAndView.setViewName("redirect:/login.jsp");
                return modelAndView;
            }
            Optional<Product> optional = productService.viewByProductId(productId);
            if (optional.isEmpty() || !activeVendor.getVendorId().equals(optional.get().getVirtualId())) {
                FlashMessages.error(session, "Product not found.");
                return modelAndView;
            }
            Product product = optional.get();
            int stock = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
            if (quantity == null || quantity < 1 || quantity > stock) {
                FlashMessages.error(session, "Enter a quantity between 1 and " + stock + ".");
                return modelAndView;
            }
            int remaining = stock - quantity;
            if (remaining <= 0) {
                productService.deleteProductById(productId);
                FlashMessages.success(session, "Removed " + quantity + " units. Product deleted (stock is 0).");
            } else {
                product.setStockQuantity(remaining);
                productService.addNewProduct(product);
                FlashMessages.success(session, "Removed " + quantity + " units. Remaining stock: " + remaining + ".");
            }
        } catch (OnlineShoppingException e) {
            FlashMessages.error(session, "An error occurred while removing stock: " + e.getMessage());
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
                FlashMessages.error(session, "Product not found with id: " + productId);
                modelAndView.setViewName("redirect:/view/all/products");
            }
        } catch (OnlineShoppingException e) {
            FlashMessages.error(session, "An error occurred while editing product: " + e.getMessage());
            modelAndView.setViewName("redirect:/view/all/products");
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
                    product.setCategory(normalizeCategory(category));
                    product.setProductName(productName);
                    product.setProductDescription(productDescription);
                    product.setPrice(price);
                    product.setStockQuantity(stockQuantity);
                    productService.addNewProduct(product);
                    FlashMessages.success(session, "Product updated successfully.");
                    modelAndView.setViewName("redirect:/view/all/update/product/" + productId);
                } else {
                    FlashMessages.error(session, "Product not found.");
                    modelAndView.setViewName("redirect:/view/all/products");
                }
            } else {
                FlashMessages.error(session, "Please login as a vendor.");
                modelAndView.setViewName("redirect:/login.jsp");
            }
        } catch (OnlineShoppingException e) {
            FlashMessages.error(session, "An error occurred while updating product: " + e.getMessage());
            modelAndView.setViewName("redirect:/view/all/products");
        }
        return modelAndView;
    }
    
    @GetMapping("/p/category/{category}")
    public ModelAndView searchProductsByCategory(@PathVariable String category,HttpSession session) {
        ModelAndView mv = new ModelAndView();
        try {
        	Vendor activeVendor = (Vendor) session.getAttribute("activeVendor");
            if (activeVendor != null) {
            	String selected = normalizeCategory(category);
            	if (selected == null || "all".equals(selected) || "allcategories".equals(selected)) {
            		mv.setViewName("redirect:/view/all/products");
            		return mv;
            	}
            	Integer vVendorId=activeVendor.getVendorId();
            	List<Product> productItems = productService.viewAllByVirtualId(vVendorId);
            	productList.clear();
            	for(Product product:productItems) {
            		String productCategory = normalizeCategory(product.getCategory());
            		boolean matches = "other".equals(selected)
            				? !ProductServiceImpl.isStandardCategory(productCategory)
            				: selected.equals(productCategory);
            		if(matches) {
            			productList.add(product);
            		}
            	}
            mv.addObject("productItems", productList);
            mv.addObject("categories", productService.listCategories());
            mv.addObject("selectedCategory", selected);
            mv.setViewName("product_view");
            }
        } catch (OnlineShoppingException e) {
            FlashMessages.error(session, "An error occurred while searching products by category: " + e.getMessage());
            mv.setViewName("redirect:/vendor");
        }
        return mv;
    }
    @GetMapping({"/p/search", "/p/{productName}"})
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
	            mv.addObject("categories", productService.listCategories());
	            mv.addObject("selectedCategory", "all");
	            mv.setViewName("product_view");
            }
        }catch (OnlineShoppingException e) {
            FlashMessages.error(session, "An error occurred while searching products.");
            mv.setViewName("redirect:/vendor");
        }
        return mv;        
    }

    /** Lowercase, trim, and turn spaces into a single slug so filters stay consistent. */
    private static String normalizeCategory(String raw) {
        if (raw == null) {
            return null;
        }
        String cleaned = raw.trim().toLowerCase().replaceAll("\\s+", "");
        return cleaned.isEmpty() ? null : cleaned;
    }
}
