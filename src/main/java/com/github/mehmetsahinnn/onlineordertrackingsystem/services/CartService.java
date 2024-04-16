package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.CartItemRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The CartService class provides methods for managing the shopping cart.
 * It includes a method for adding a product to the cart.
 */
@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    /**
     * Constructs a new CartService with the specified CartRepository, CartItemRepository, CustomerService, and ProductService.
     *
     * @param cartItemRepository the CartItemRepository to be used by the CartService
     * @param customerService    the CustomerService to be used by the CartService
     * @param productService     the ProductService to be used by the CartService
     */
    @Autowired
    public CartService( CartItemRepository cartItemRepository, CustomerService customerService, ProductService productService) {

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
    public CartItem addToCart(Product product, int quantity) {
        checkStock(product, quantity);
        Customer currentCustomer = customerService.getCurrentUser();
        CartItem cartItem = updateCartWithProduct(product, currentCustomer, quantity);
        cartItemRepository.save(cartItem);
        return cartItem;
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

    private CartItem updateCartWithProduct(Product product, Customer currentCustomer, int quantity) {
        CartItem cartItem = cartItemRepository.findByProductAndCustomer(product, currentCustomer);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(null, currentCustomer, product, quantity);
        }
        return cartItem;
    }

    public List<CartItem> findAllItems() {
        return cartItemRepository.findAll();
    }
}