package com.github.mehmetsahinnn.onlineordertrackingsystem.cart;

import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart.Cart;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart.CartRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart.CartService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem.CartItemRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.customer.CustomerService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
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
    private CartRepository cartRepository;

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

        Cart cart = new Cart();
        when(customerService.getCurrentUser()).thenReturn(null);
        when(cartRepository.findCartByCustomer(null)).thenReturn(cart);
        when(cartItemRepository.findByProductAndCart(product, cart)).thenReturn(null);

        Cart result = cartService.addToCart(product, 1);

        assertEquals(cart, result);
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

        doNothing().when(cartRepository).deleteById(cartItemId);

        cartService.deleteCartItemById(cartItemId);

        verify(cartRepository, times(1)).deleteById(cartItemId);
    }
    /**
     * Tests the {@link CartService#deleteCartById(Long)} method to ensure that it correctly deletes a cart
     * with the specified ID.
     */
    @Test
    public void testDeleteCartById() {
        Long cartId = 1L;

        doNothing().when(cartRepository).deleteById(cartId);

        cartService.deleteCartById(cartId);

        verify(cartRepository, times(1)).deleteById(cartId);
    }
}