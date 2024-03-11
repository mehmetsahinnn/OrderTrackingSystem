package com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart;

import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Adds a product to the cart.
     *
     * @param productId the ID of the product to add to the cart
     * @param quantity the quantity of the product to add to the cart
     * @return a ResponseEntity containing the updated cart and the HTTP status
     */
    @PostMapping("/{productId}/{quantity}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId, @PathVariable int quantity) {
        try {
            Product product = productService.getProductById(productId);
            Cart updatedCart = cartService.addToCart(product, quantity);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a shopping cart with the specified ID.
     *
     * @param id The ID of the shopping cart to be deleted
     * @return HTTP response; HttpStatus.NO_CONTENT if the operation is successful, otherwise HttpStatus.INTERNAL_SERVER_ERROR
     */
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id){
        try {
            cartService.deleteCartById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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