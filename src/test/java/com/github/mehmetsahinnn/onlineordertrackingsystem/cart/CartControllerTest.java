package com.github.mehmetsahinnn.onlineordertrackingsystem.cart;

import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart.Cart;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart.CartController;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart.CartService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
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
        Product product = new Product(1L, "Test Product", "Desc","Category 1", 50.99,80);
        Cart updatedCart = new Cart();
        when(productService.getProductById(1L)).thenReturn(product);
        when(cartService.addToCart(product, 2)).thenReturn(updatedCart);

        ResponseEntity<?> responseEntity = cartController.addToCart(1L, 2);

        assertEquals("", HttpStatus.OK.toString(), responseEntity.getStatusCode().toString());
        assertEquals("", updatedCart.toString(), Objects.requireNonNull(responseEntity.getBody()).toString());
    }

    /**
     * Tests the deleteCart method of CartController when cart deletion is successful.
     * Verifies that deleting a cart returns a NO_CONTENT response.
     */
    @Test
    public void testDeleteCartSuccessful() {
        long cartId = 1L;
        CartService cartService = mock(CartService.class);
        doNothing().when(cartService).deleteCartById(cartId);
        CartController cartController = new CartController(cartService, null);

        ResponseEntity<?> response = cartController.deleteCart(cartId);

        assertEquals("",HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService, times(1)).deleteCartById(cartId);
    }

    /**
     * Tests the deleteCart method of CartController when cart deletion fails.
     * Verifies that deleting a cart returns a NO_CONTENT response.
     */
    @Test
    public void testDeleteCartFailure() {
        long cartId = 1L;
        CartService cartService = mock(CartService.class);
        doNothing().when(cartService).deleteCartById(cartId);
        CartController cartController = new CartController(cartService, null);

        ResponseEntity<?> response = cartController.deleteCart(cartId);

        assertEquals("",HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService, times(1)).deleteCartById(cartId);
    }
}
