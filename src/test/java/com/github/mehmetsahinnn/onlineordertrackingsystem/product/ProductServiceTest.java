package com.github.mehmetsahinnn.onlineordertrackingsystem.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        productService = new ProductService(productRepository);
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        product.setName("Product 1");
        product.setPrice(10.99);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setPrice(10.99);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Product retrievedProduct = productService.getProductById(product.getId());

        assertEquals(product.getId(), retrievedProduct.getId());
        assertEquals(product.getName(), retrievedProduct.getName());
        assertEquals(product.getPrice(), retrievedProduct.getPrice());
    }

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
    public void testUpdateStock() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setPrice(10.99);
        product.setNumberInStock(5);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.updateStock(product.getId(), 10);

        assertEquals(10, product.getNumberInStock());
    }

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