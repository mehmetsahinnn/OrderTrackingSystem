package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.CustomUpdateStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.InsufficientStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The ProductService class provides services for managing products.
 * It includes services for creating, retrieving, updating, and deleting products.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    /**
     * Constructs a new ProductService with the specified ProductRepository.
     *
     * @param productRepository the ProductRepository to be used by the ProductService
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Saves a product.
     *
     * @param product the product to be saved
     * @return the saved product
     */
    public Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            logger.error("Error occurred while saving the product", e);
            throw new RuntimeException("Error occurred while saving the product", e);
        }
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting the product with id: " + id, e);
            throw new RuntimeException("Error occurred while deleting the product with id: " + id, e);
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the retrieved product
     */
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Error occurred while retrieving the product with id: " + id);
        }
        return optionalProduct.get();
    }

    /**
     * Searches for products based on the specified category and price range.
     *
     * @param category the category of the products to search for
     * @param minPrice the minimum price of the products to search for
     * @param maxPrice the maximum price of the products to search for
     * @return the list of matching products
     */
    public List<Product> searchProducts(String category, Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range. Max price must be greater than min price.");
        }
        try {
            if (category != null) {
                return searchProductsByCategoryAndPrice(category, minPrice, maxPrice);
            } else {
                return searchProductsByPrice(minPrice, maxPrice);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error occurred while searching for products with category: {}, minPrice: {}, maxPrice: {}", category, minPrice, maxPrice, e);
            throw new IllegalArgumentException("Error occurred while searching for products", e);
        }
    }

    /**
     * Searches for products based on the specified category and price range.
     *
     * @param category the category of the products to search for
     * @param minPrice the minimum price of the products to search for
     * @param maxPrice the maximum price of the products to search for
     * @return the list of matching products
     */
    private List<Product> searchProductsByCategoryAndPrice(String category, Double minPrice, Double maxPrice) {
        try {
            if (minPrice != null && maxPrice == null) {
                return productRepository.findByCategoryAndPriceGreaterThan(category, minPrice);
            } else if (minPrice == null && maxPrice != null) {
                return productRepository.findByCategoryAndPriceLessThan(category, maxPrice);
            } else if (minPrice == null) {
                return productRepository.findByCategory(category);
            } else {
                return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
            }
        } catch (Exception e) {
            logger.error("Error occurred while searching for products by category and price", e);
            throw new RuntimeException("Error occurred while searching for products by category and price", e);
        }
    }

    /**
     * Searches for products based on the specified price range.
     *
     * @param minPrice the minimum price of the products to search for
     * @param maxPrice the maximum price of the products to search for
     * @return the list of matching products
     */
    private List<Product> searchProductsByPrice(Double minPrice, Double maxPrice) {
        try {
            if (minPrice != null && maxPrice == null) {
                return productRepository.findByPriceGreaterThan(minPrice);
            } else if (minPrice == null && maxPrice != null) {
                return productRepository.findByPriceLessThan(maxPrice);
            } else {
                return productRepository.findAll();
            }
        } catch (Exception e) {
            logger.error("Error occurred while searching for products by price", e);
            throw new RuntimeException("Error occurred while searching for products by price", e);
        }
    }

    /**
     * Retrieves all products.
     *
     * @return the list of all products
     */
    public List<Product> findAll() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving all products", e);
        }
    }

    /**
     * Updates the product identified by the given ID with the provided updated product details.
     *
     * @param id             the ID of the product to be updated
     * @param updatedProduct the updated product information
     * @return the updated product entity if the update operation is successful
     * @throws RuntimeException if no product is found with the given ID
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setNumberInStock(updatedProduct.getNumberInStock());
        existingProduct.setCategory(updatedProduct.getCategory());

        return saveProduct(existingProduct);
    }

    public List<Product> findProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}



