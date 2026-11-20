package com.genpact.online_shopping_app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import com.genpact.online_shopping_app.controller.AdminController;
import com.genpact.online_shopping_app.entity.Role;
import com.genpact.online_shopping_app.entity.User;
import com.genpact.online_shopping_app.entity.Vendor;
import com.genpact.online_shopping_app.exception.OnlineShoppingException;
import com.genpact.online_shopping_app.service.UserService;
import com.genpact.online_shopping_app.service.VendorService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private VendorService vendorService;
    @Mock
    private UserService userService;
    @Mock
    private HttpSession session;

    @InjectMocks
    private AdminController adminController;

    private List<Vendor> vendorList;
    private List<User> userList;

    @BeforeEach
    public void setup() {
        vendorList = new ArrayList<>();
        Role role = new Role("Vendor");
        vendorList.add(new Vendor("Vendor 1", "vendor1@example.com", "password1", "1234567890", role));
        vendorList.add(new Vendor("Vendor 2", "vendor2@example.com", "password2", "0987654321", role));
        
        userList = new ArrayList<>();
        Role userRole = new Role("User");
        userList.add(new User("User 1", "user1@example.com", "password1", "1234567890", userRole));
        userList.add(new User("User 2", "user2@example.com", "password2", "0987654321", userRole));
    }

    @Test
    public void testVendorsList_Success() throws OnlineShoppingException {
        when(vendorService.getAllVendors()).thenReturn(vendorList);
        ModelAndView mv = adminController.vendorsList(session);
        assertEquals("admin_vendors_view", mv.getViewName());
        List<Vendor> returnedVendorList = (List<Vendor>) mv.getModel().get("vendorList");
        assertEquals(vendorList, returnedVendorList);
    }
    @Test
    public void testUsersList_Success() throws OnlineShoppingException {
        when(userService.getAllUsers()).thenReturn(userList);
        ModelAndView mv = adminController.usersList(session);
        assertEquals("admin_users_view", mv.getViewName());
        List<User> returnedUserList = (List<User>) mv.getModel().get("userList");
        assertEquals(userList, returnedUserList);
    }   
}
