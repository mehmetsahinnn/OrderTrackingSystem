package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.CartItemRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CartService class.
 */
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;


    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    /**
     * Initializes Mockito annotations before each test method.
     */
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the addToCart method of CartService.
     * Verifies that adding a product to the cart returns the updated cart.
     */
    @Test
    public void addToCartTest() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setNumberInStock(10);

        CartItem cartItem = new CartItem();
        when(customerService.getCurrentUser()).thenReturn(null);
        when(cartItemRepository.findByProductAndCustomer(product, null)).thenReturn(null);

        CartItem result = cartService.addToCart(product, 1);

        assertEquals(cartItem, result);
        verify(productService, times(1)).saveProduct(product);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    /**
     * Tests the {@link CartService#deleteCartItemById(Long)} method to ensure that it correctly deletes a cart item
     * with the specified ID.
     */
    @Test
    public void testDeleteCartItemById() {
        Long cartItemId = 1L;

        doNothing().when(cartItemRepository).deleteById(cartItemId);

        cartService.deleteCartItemById(cartItemId);

        verify(cartItemRepository, times(1)).deleteById(cartItemId);
    }

}