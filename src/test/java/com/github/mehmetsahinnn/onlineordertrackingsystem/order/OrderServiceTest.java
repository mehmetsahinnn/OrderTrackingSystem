package com.github.mehmetsahinnn.onlineordertrackingsystem.order;

import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;

    @BeforeEach
    public void init() {
        System.out.println(" ");
    }

    @Test
    public void testPlaceOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setQuantity(10);
        order.setStatus(OrderStatus.CONFIRMED);

        Product product = new Product();
        product.setId(1L);
        product.setNumberInStock(20);
        order.setProduct(product);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        doNothing().when(productService).updateStock(any(Long.class), any(Integer.class));

        Order placedOrder = orderService.placeOrder(order);

        verify(orderRepository, times(1)).save(order);
        verify(productService, times(1)).updateStock(product.getId(), product.getNumberInStock() - order.getQuantity());

        assertEquals(order.getId(), placedOrder.getId());
        assertEquals(order.getQuantity(), placedOrder.getQuantity());
        assertEquals(order.getStatus(), placedOrder.getStatus());
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertEquals(order.getId(), foundOrder.getId());
    }

    @Test
    public void testGetAllOrders() {
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getAllOrders();

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testSaveOrder() {
        Order order = new Order();
        Product product = new Product();
        product.setId(1L);
        product.setNumberInStock(100);

        order.setId(1L);
        order.setProduct(product);
        order.setQuantity(10);


        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.placeOrder(order);

        assertEquals(order.getId(), savedOrder.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;

        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}