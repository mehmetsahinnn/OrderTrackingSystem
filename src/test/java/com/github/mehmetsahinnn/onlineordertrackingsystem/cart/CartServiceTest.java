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

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

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
}