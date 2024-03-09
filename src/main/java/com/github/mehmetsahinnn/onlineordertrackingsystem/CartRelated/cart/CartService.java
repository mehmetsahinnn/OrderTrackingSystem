package com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart;

import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem.CartItemRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.customer.CustomerService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The CartService class provides methods for managing the shopping cart.
 * It includes a method for adding a product to the cart.
 */
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    /**
     * Constructs a new CartService with the specified CartRepository and UserService.
     *
     * @param cartRepository  the CartRepository to be used by the CartService
     * @param customerService the CustomerService to be used by the CartService
     */
    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, CustomerService customerService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    /**
     * Adds a product to the cart.
     *
     * @param product  the product to add to the cart
     * @param quantity the quantity of the product to add to the cart
     * @return the updated cart
     */
    public Cart addToCart(Product product, int quantity) {
        checkStock(product, quantity);
        Cart currentCart = cartRepository.findCartByCustomer(customerService.getCurrentUser());
        CartItem cartItem = updateCartWithProduct(product, currentCart, quantity);
        cartItemRepository.save(cartItem);
        return cartRepository.findCartByCustomer(customerService.getCurrentUser());
    }

    private void checkStock(Product product, int quantity) {
        if (product.getNumberInStock() < quantity) {
            throw new RuntimeException("Product is out of stock");
        }
        product.setNumberInStock(product.getNumberInStock() - quantity);
        productService.saveProduct(product);
    }

    private CartItem updateCartWithProduct(Product product, Cart currentCart, int quantity) {
        CartItem cartItem = cartItemRepository.findByProductAndCart(product, currentCart);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(null, currentCart, product, quantity);
        }
        return cartItem;
    }
}