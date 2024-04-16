package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Cart;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.CartService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * Unit tests for the CartController class.
 */
public class CartControllerTest {
    @InjectMocks
    CartController cartController;
    @Mock
    CartService cartService;
    @Mock
    ProductService productService;

    /**
     * Initializes Mockito annotations before each test method.
     */
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the addToCart method of CartController.
     * Verifies that adding a product to the cart returns an OK response with the updated cart.
     */
    @Test
    void testAddToCart() {
        Product product = new Product(1L, "Test Product", "Desc", "Category 1", 50.99, 80);
        CartItem updatedCartItem = new CartItem();
        when(productService.getProductById(1L)).thenReturn(product);
        when(cartService.addToCart(product, 2)).thenReturn(updatedCartItem);

        ResponseEntity<?> responseEntity = cartController.addToCart(1L, 2);

        assertEquals("", HttpStatus.OK.toString(), responseEntity.getStatusCode().toString());
        assertEquals("", updatedCartItem.toString(), Objects.requireNonNull(responseEntity.getBody()).toString());
    }

}
