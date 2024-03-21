package com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart;

import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem.CartItemRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.customer.CustomerService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * Constructs a new CartService with the specified CartRepository, CartItemRepository, CustomerService, and ProductService.
     *
     * @param cartRepository     the CartRepository to be used by the CartService
     * @param cartItemRepository the CartItemRepository to be used by the CartService
     * @param customerService    the CustomerService to be used by the CartService
     * @param productService     the ProductService to be used by the CartService
     */
    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, CustomerService customerService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    /**
     * Retrieves all the carts from the database.
     *
     * @return a List of Cart objects representing all the carts found in the database.
     * @throws RuntimeException if an error occurs while fetching the carts from the database.
     */
    public List<Cart> findAll() {
        try {
            return cartRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching carts", e);
        }
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

    /**
     * Deletes a cart with the specified ID.
     *
     * @param id the ID of the cart to be deleted
     * @throws RuntimeException if an error occurs while deleting the cart
     */
    public void deleteCartById(Long id) {
        try {
            cartRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting the cart with id: " + id, e);
        }
    }

    /**
     * Deletes a cart item with the specified ID.
     *
     * @param id the ID of the cart item to be deleted
     * @throws RuntimeException if an error occurs while deleting the cart item
     */
    public void deleteCartItemById(Long id) {
        try {
            cartItemRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting the cart item with id: " + id, e);
        }
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