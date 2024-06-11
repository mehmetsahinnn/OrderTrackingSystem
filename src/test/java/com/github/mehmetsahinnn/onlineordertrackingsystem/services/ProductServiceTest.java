package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProductService class.
 */
@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    /**
     * Initializes the ProductService before each test.
     */
    @BeforeEach
    public void init() {
        productService = new ProductService(productRepository);
    }
    /**
     * Tests the saveProduct method of ProductService.
     */
    @Test
    public void test_save_product_success() {
        // Arrange
        ProductRepository mockRepository = Mockito.mock(ProductRepository.class);
        ProductService productService = new ProductService(mockRepository);
        Product product = new Product(1L, "Laptop", "High-end gaming laptop", "Electronics", 1500.00, 10);
        when(mockRepository.save(Mockito.any(Product.class))).thenReturn(product);

        // Act
        Product savedProduct = productService.saveProduct(product);

        // Assert
        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
    }

    @Test
    public void test_save_product_exception() {
        // Arrange
        ProductRepository mockRepository = Mockito.mock(ProductRepository.class);
        ProductService productService = new ProductService(mockRepository);
        Product product = new Product(1L, "Laptop", "High-end gaming laptop", "Electronics", 1500.00, 10);
        Mockito.when(mockRepository.save(Mockito.any(Product.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> productService.saveProduct(product));
        assertEquals("Error occurred while saving the product", exception.getMessage());
    }

    /**
     * Tests the deleteProduct method of ProductService.
     */
    @Test
    public void testSearchProducts_invalid_price_range() {
        // Arrange
        String category = "Electronics";
        double minPrice = 30.0;
        double maxPrice = 10.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.searchProducts(category, minPrice, maxPrice));
        assertEquals("Invalid price range. Max price must be greater than min price.", exception.getMessage());
    }

        /**
         * Tests the getProductById method of ProductService.
         */
        @Test
        public void testFindProductsByCategory() {
            Product product1 = new Product();
            product1.setId(1L);
            product1.setName("Product 1");
            product1.setPrice(10.99);
            product1.setCategory("Electronics");

            Product product2 = new Product();
            product2.setId(2L);
            product2.setName("Product 2");
            product2.setPrice(20.99);
            product2.setCategory("Electronics");

            List<Product> products = Arrays.asList(product1, product2);

            when(productRepository.findByCategory(anyString())).thenReturn(products);

            List<Product> retrievedProducts = productService.findProductsByCategory("Electronics");

            assertEquals(2, retrievedProducts.size());
            assertEquals(product1.getId(), retrievedProducts.get(0).getId());
            assertEquals(product2.getId(), retrievedProducts.get(1).getId());
        }
    /**
     * Tests the searchProducts method of ProductService.
     */
    @Test
    public void testSearchProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(10.99);
        product1.setCategory("Electronics");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(20.99);
        product2.setCategory("Electronics");

        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findByCategoryAndPriceBetween(anyString(), anyDouble(), anyDouble())).thenReturn(products);

        List<Product> retrievedProducts = productService.searchProducts("Electronics", 10.0, 30.0);

        assertEquals(2, retrievedProducts.size());
        assertEquals(product1.getId(), retrievedProducts.get(0).getId());
        assertEquals(product2.getId(), retrievedProducts.get(1).getId());
    }

    @Test
    public void testSearchProducts_invalid_price_range_exception() {
        // Arrange
        String category = "Electronics";
        double minPrice = 30.0;
        double maxPrice = 10.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.searchProducts(category, minPrice, maxPrice));
        assertEquals("Invalid price range. Max price must be greater than min price.", exception.getMessage());
    }

    /**
     * Tests the updateStock method of ProductService.
     */
    @Test
    public void testUpdateStock_increase_stock() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setPrice(10.99);
        product.setNumberInStock(5);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.updateStock(product.getId(), 10);

        assertEquals(15, product.getNumberInStock());
    }
    /**
     * Tests the findAll method of ProductService.
     */
    @Test
    public void testFindAll() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(10.99);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(20.99);

        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> retrievedProducts = productService.findAll();

        assertEquals(2, retrievedProducts.size());
        assertEquals(product1.getId(), retrievedProducts.get(0).getId());
        assertEquals(product2.getId(), retrievedProducts.get(1).getId());
    }
}