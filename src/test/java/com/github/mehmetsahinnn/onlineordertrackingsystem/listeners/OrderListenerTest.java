package com.github.mehmetsahinnn.onlineordertrackingsystem.listeners;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.InsufficientStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners.OrderListener;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.ProductRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.OrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class OrderListenerTest {

    @InjectMocks
    private OrderListener orderListener;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void handleMessage_success() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description", "Category", 10.0, 10);
        OrderItem orderItem = new OrderItem(1L, null, product, 5, 50.0);
        Order order = new Order();
        order.setOrderItems(List.of(orderItem));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        orderListener.handleMessage(order);

        // Assert
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(LocalDate.now().plusDays(5), order.getEstimatedDeliveryDate());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertEquals(5, savedProduct.getNumberInStock());

        verify(orderRepository).save(order);
    }

    @Test
    public void insufficientStockException() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description", "Category", 10.0, 2);
        OrderItem orderItem = new OrderItem(1L, null, product, 5, 50.0);
        Order order = new Order();
        order.setOrderItems(List.of(orderItem));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () -> {
            orderService.placeOrder(order);
        });

        assertEquals("Insufficient stock for product id: 1", exception.getMessage());

        verify(orderRepository, never()).save(any(Order.class));
        verify(productRepository, never()).save(any(Product.class));
    }


    @Test
    public void productNotFoundException() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description", "Category", 10.0, 10);
        OrderItem orderItem = new OrderItem(1L, null, product, 5, 50.0);
        Order order = new Order();
        order.setOrderItems(List.of(orderItem));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderListener.handleMessage(order);
        });

        assertEquals("Product not found with id: 1", exception.getMessage());

        verify(orderRepository, never()).save(any(Order.class));
    }
}