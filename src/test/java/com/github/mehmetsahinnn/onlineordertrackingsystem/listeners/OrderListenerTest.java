package com.github.mehmetsahinnn.onlineordertrackingsystem.listeners;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners.OrderListener;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class OrderListenerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderListener orderListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleMessageOrderProcessedAndStockUpdated() {
        Product product = new Product();
        product.setId(1L);
        product.setNumberInStock(10);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(5);

        Order order = new Order();
        order.setOrderItems(Collections.singletonList(orderItem));
        orderListener.handleMessage(order);

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(LocalDate.now().plusDays(5), order.getEstimatedDeliveryDate());

        verify(productService, times(1)).updateStock(product.getId(), -orderItem.getQuantity());
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        assertEquals(OrderStatus.CONFIRMED, orderCaptor.getValue().getStatus());
    }

    @Test
    public void testHandleMessageInsufficientStock() {
        Product product = new Product();
        product.setId(1L);
        product.setNumberInStock(2);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(5);

        Order order = new Order();
        order.setOrderItems(Collections.singletonList(orderItem));

        orderListener.handleMessage(order);

        verify(productService, never()).updateStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }
}
