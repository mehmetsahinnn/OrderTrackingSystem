package com.github.mehmetsahinnn.onlineordertrackingsystem.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void init() {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {
            Product product1 = new Product();
            product1.setId(1L);
            product1.setName("Product 1");

            Product product2 = new Product();
            product2.setId(2L);
            product2.setName("Product 2");

            List<Product> products = Arrays.asList(product1, product2);

            when(productService.findAll()).thenReturn(products);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(10.99);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(20.99);

        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(productService.findAll()).thenReturn(expectedProducts);

        ResponseEntity<List<Product>> response = productController.listProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    public void testSearchProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(10.99);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(20.99);

        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(productService.searchProducts(anyString(), anyDouble(), anyDouble())).thenReturn(expectedProducts);

        ResponseEntity<List<Product>> response = productController.searchProducts("Category 1", 10.0, 20.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    public void testCreateProduct() {
        Product productToCreate = new Product();
        productToCreate.setName("Product 1");
        productToCreate.setPrice(10.99);

        Product createdProduct = new Product();
        createdProduct.setId(1L);
        createdProduct.setName("Product 1");
        createdProduct.setPrice(10.99);

        when(productService.saveProduct(productToCreate)).thenReturn(createdProduct);

        ResponseEntity<Product> response = productController.createProduct(productToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdProduct, response.getBody());
    }

    @Test
    public void getProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setPrice(10.99);


        when(productService.getProductById(product.getId())).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById(product.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testUpdateProduct() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Existing Product");
        existingProduct.setPrice(10.99);

        Product productToUpdate = new Product();
        productToUpdate.setName("Updated Product");
        productToUpdate.setPrice(20.99);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(20.99);

        when(productService.getProductById(existingProduct.getId())).thenReturn(existingProduct);
        when(productService.saveProduct(any(Product.class))).thenReturn(updatedProduct);

        ResponseEntity<Product> response = productController.updateProduct(existingProduct.getId(), productToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProduct, response.getBody());
    }

    @Test
    public void testDeleteProduct(){
        Product product = new Product();
        product.setId(1L);
        product.setName("Existing Product");
        product.setPrice(10.99);

        ResponseEntity<Void> response = productController.deleteProduct(product.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }


}