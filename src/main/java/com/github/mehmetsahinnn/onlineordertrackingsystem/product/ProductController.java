package com.github.mehmetsahinnn.onlineordertrackingsystem.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The Product Controller class provides REST ful API endpoints for managing products.
 * It includes endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

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
     *         Returns HttpStatus.OK if the operation is successful.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an error occurs during the operation.
     */
    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        try {
            List<Product> products = productService.findAll();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Searches for products based on the specified category and price range.
     *
     * @param category the category of the products to search for
     * @param minPrice the minimum price of the products to search for
     * @param maxPrice the maximum price of the products to search for
     * @return a ResponseEntity containing the list of matching products and the HTTP status
     */
    @GetMapping("/category")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String category, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice) {
        try {
            List<Product> products = productService.searchProducts(category, minPrice, maxPrice);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new product.
     *
     * @param product the product to be created
     * @return a ResponseEntity containing the created product and the HTTP status
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.saveProduct(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return a ResponseEntity containing the retrieved product and the HTTP status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                throw new RuntimeException("Product not found with id: " + id);
            }
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the product with the specified ID using the provided product details.
     *
     * @param id      the ID of the product to be updated
     * @param product the updated product information
     * @return a ResponseEntity containing the updated product with HTTP status 200 OK if successful,
     *         or a ResponseEntity with HTTP status 404 NOT FOUND if no product is found with the given ID,
     *         or a ResponseEntity with HTTP status 500 INTERNAL SERVER ERROR if an unexpected error occurs
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * Deletes a product.
     *
     * @param id the ID of the product to delete
     * @return a ResponseEntity containing the HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}