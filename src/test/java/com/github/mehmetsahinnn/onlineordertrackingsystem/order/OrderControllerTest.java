package com.github.mehmetsahinnn.onlineordertrackingsystem.order;

import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;
    @Mock
    ProductService productService;
    @Mock
    OrderStatus orderStatus;

    @BeforeEach
    public void init() {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {
            Order order = new Order();
            Order order1 = new Order();

            List<Order> orders = Arrays.asList(order, order1);

            when(orderService.getAllOrders()).thenReturn(orders);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void placeOrderTest(){
        Order wantedOrder = new Order();
        wantedOrder.setQuantity(10);
        wantedOrder.setStatus(OrderStatus.CONFIRMED);

        Order requestedOrder = new Order();
        requestedOrder.setId(1L);
        requestedOrder.setQuantity(10);
        requestedOrder.setStatus(OrderStatus.CONFIRMED);

        when(orderService.placeOrder(wantedOrder)).thenReturn(requestedOrder);

        ResponseEntity<Order> response = orderController.placeOrder(wantedOrder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(requestedOrder, response.getBody());
    }

    @Test
    public void getAllOrdersTest(){
        Order order1 = new Order();
        order1.setId(1L);
        order1.setQuantity(10);
        order1.setStatus(OrderStatus.CONFIRMED);

        Order order2 = new Order();
        order2.setId(1L);
        order2.setQuantity(10);
        order2.setStatus(OrderStatus.CONFIRMED);

        List<Order> expectedOrders = Arrays.asList(order1,order2);

        when(orderService.getAllOrders()).thenReturn(expectedOrders);

        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrders, response.getBody());

    }

    @Test
    public void getOrderByIdTest(){
        Order order = new Order();
        order.setId(1L);
        order.setQuantity(10);
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderService.getOrderById(order.getId())).thenReturn(order);

        ResponseEntity<Order> response = orderController.getOrderById(order.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    @Test
    public void updateOrderTest(){
        Order baseOrder = new Order();
        baseOrder.setId(1L);
        baseOrder.setQuantity(10);
        baseOrder.setStatus(OrderStatus.SHIPPED);

        Order orderToBeUpdated = new Order();
        orderToBeUpdated.setQuantity(10);
        orderToBeUpdated.setStatus(OrderStatus.DELIVERED);

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setQuantity(10);
        updatedOrder.setStatus(OrderStatus.DELIVERED);

        when(orderService.getOrderById(baseOrder.getId())).thenReturn(baseOrder);
        when(orderService.updateOrder(baseOrder.getId(), orderToBeUpdated)).thenReturn(updatedOrder);

        ResponseEntity<Order> response = orderController.updateOrder(baseOrder.getId(), orderToBeUpdated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrder, response.getBody());
    }

    @Test
    void deleteOrderTest() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        Product product = new Product();
        product.setNumberInStock(10);
        order.setProduct(product);
        order.setQuantity(1);
        when(orderService.getOrderById(orderId)).thenReturn(order);

        ResponseEntity<Void> responseEntity = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(orderService, times(1)).deleteOrder(orderId);
        assertEquals(11, product.getNumberInStock());
    }


    @Test
    void testGetEstimatedDeliveryDate() {
        Order order = new Order();
        order.setId(1L);
        order.setEstimatedDeliveryDate(LocalDate.now());
        when(orderService.getOrderById(1L)).thenReturn(order);

        ResponseEntity<?> responseEntity = orderController.getEstimatedDeliveryDate(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(order.getEstimatedDeliveryDate(), responseEntity.getBody());
    }



}
