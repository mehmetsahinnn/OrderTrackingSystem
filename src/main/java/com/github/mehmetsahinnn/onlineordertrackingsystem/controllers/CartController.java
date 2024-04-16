package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Cart;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.CartService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.ResponseHandler;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The CartController class provides RESTful API endpoints for managing the shopping cart.
 * It includes an endpoint for adding a product to the cart.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    /**
     * Constructs a new CartController with the specified CartService.
     *
     * @param cartService the CartService to be used by the CartController
     */
    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Retrieves all the carts and returns them as a ResponseEntity.
     *
     * @return a ResponseEntity containing the list of carts if retrieval is successful,
     * or an error message with HTTP status 500 if an error occurs during retrieval.
     */
    @GetMapping
    public ResponseEntity<?> listCarts() {
        try {
            List<Cart> carts = cartService.findAll();
            return ResponseHandler.generateResponse("Carts", HttpStatus.OK, carts);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while retrieving carts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Adds a product to the cart.
     *
     * @param productId the ID of the product to add to the cart
     * @param quantity  the quantity of the product to add to the cart
     * @return a ResponseEntity containing the updated cart and the HTTP status
     */
    @PostMapping("/{productId}/{quantity}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId, @PathVariable int quantity) {
        try {
            Product product = productService.getProductById(productId);
            Cart updatedCart = cartService.addToCart(product, quantity);
            return ResponseHandler.generateResponse("Item added to cart", HttpStatus.OK, updatedCart);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("An error occurred while adding item to cart", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * Deletes a shopping cart with the specified ID.
     *
     * @param id The ID of the shopping cart to be deleted
     * @return HTTP response; HttpStatus.NO_CONTENT if the operation is successful, otherwise HttpStatus.INTERNAL_SERVER_ERROR
     */
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCartById(id);
            return ResponseHandler.generateResponse("Item deleted", HttpStatus.NO_CONTENT, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("An error occurred while deleting an item", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    /**
     * Deletes a shopping cart item with the specified ID.
     *
     * @param id The ID of the shopping cart item to be deleted
     * @return HTTP response; HttpStatus.NO_CONTENT if the operation is successful, otherwise HttpStatus.INTERNAL_SERVER_ERROR
     */
    @DeleteMapping("/cartitem/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id){
        try {
            cartService.deleteCartItemById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}