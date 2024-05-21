package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.ProductService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.ResponseEntity;

/**
 * The Product Controller class provides REST ful API endpoints for managing products.
 * It includes endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequestMapping("/api/products")
@Log4j2
public class ProductController extends BaseController {

    private final ProductService productService;
    Logger logger = LogManager.getLogger();

    /**
     * Constructs a new ProductController with the specified ProductService.
     *
     * @param productService the ProductService to be used by the ProductController
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products.
     *
     * @return a ResponseEntity containing the list of all products and the HTTP status.
     * Returns HttpStatus.OK if the operation is successful.
     * Returns HttpStatus.INTERNAL_SERVER_ERROR if an error occurs during the operation.
     */
    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        return handleRequest(productService::findAll, "All products listed");
    }

    /**
     * Searches for products based on category and price range.
     *
     * @param category the category of the products to search for
     * @param minPrice the minimum price of the products to search for
     * @param maxPrice the maximum price of the products to search for
     * @return a ResponseEntity containing the list of matching products and the HTTP status.
     * Returns HttpStatus.OK if the operation is successful.
     * Returns HttpStatus.INTERNAL_SERVER_ERROR if an error occurs during the operation.
     */
    @GetMapping("/category")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String category, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice) {
        return handleRequest(() -> productService.searchProducts(category, minPrice, maxPrice), "Searched products by category and price range");
    }

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return a ResponseEntity containing the created product and the HTTP status.
     * Returns HttpStatus.OK if the operation is successful.
     * Returns HttpStatus.INTERNAL_SERVER_ERROR if an error occurs during the operation.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return handleRequest(() -> productService.saveProduct(product), "Created product");
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return a ResponseEntity containing the retrieved product and the HTTP status.
     * Returns HttpStatus.OK if the operation is successful.
     * Returns HttpStatus.INTERNAL_SERVER_ERROR if an error occurs during the operation.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return handleRequest(() -> productService.getProductById(id), "Retrieved product by ID");
    }

    /**
     * Updates a product.
     *
     * @param id      the ID of the product to update
     * @param product the updated product information
     * @return a ResponseEntity containing the updated product and the HTTP status.
     * Returns HttpStatus.OK if the operation is successful.
     * Returns HttpStatus.INTERNAL_SERVER_ERROR if an error occurs during the operation.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return handleRequest(() -> productService.updateProduct(id, product), "Updated Product");
    }

    /**
     * Deletes a product.
     *
     * @param id the ID of the product to delete
     * @return a ResponseEntity containing the HTTP status.
     * Returns HttpStatus.OK if the operation is successful.
     * Returns HttpStatus.INTERNAL_SERVER_ERROR if an error occurs during the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return handleRequest(() -> {
            productService.deleteProduct(id);
            return null;
        }, "Deleted Product");
    }
}