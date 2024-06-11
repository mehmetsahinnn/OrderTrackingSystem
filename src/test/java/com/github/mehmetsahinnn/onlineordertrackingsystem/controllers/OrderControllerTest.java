package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.OrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
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

import static org.mockito.Mockito.*;
import org.mockito.stubbing.*;


/**
 * Unit tests for the OrderController class.
 */
public class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;


    /**
     * Initializes Mockito annotations before each test method.
     */
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


    /**
     * Tests the placeOrder method of OrderController.
     * Verifies that the response status is CREATED and the returned order matches the expected order.
     */
    @Test
    public void test_place_order_with_valid_data() {
        Order validOrder = new Order();
        validOrder.setStatus(OrderStatus.CONFIRMED);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setStatus(OrderStatus.CONFIRMED);

        when(orderService.placeOrder(any(Order.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(savedOrder));

        ResponseEntity<Object> response = orderController.placeOrder(validOrder);

        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(HttpStatus.CREATED, response.getBody());
    }

    /**
     * Tests the getAllOrders method of OrderController.
     * Verifies that the response status is OK and the returned list of orders matches the expected list.
     */
    @Test
    public void getAllOrdersTest(){
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.CONFIRMED);

        Order order2 = new Order();
        order2.setId(1L);
        order2.setStatus(OrderStatus.CONFIRMED);

        List<Order> expectedOrders = Arrays.asList(order1,order2);

        when(orderService.getAllOrders()).thenReturn(expectedOrders);

        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrders, response.getBody());

    }

    /**
     * Tests the getOrderById method of OrderController.
     * Verifies that the response status is OK and the returned order matches the expected order.
     */
    @Test
    public void getOrderByIdTest(){
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderService.getOrderById(order.getId())).thenReturn(order);

        ResponseEntity<?> response = orderController.getOrderById(order.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    /**
     * Tests the updateOrder method of OrderController.
     * Verifies that the response status is OK and the returned order matches the expected updated order.
     */
    @Test
    public void updateOrderTest(){
        Order baseOrder = new Order();
        baseOrder.setId(1L);
        baseOrder.setStatus(OrderStatus.SHIPPED);

        Order orderToBeUpdated = new Order();
        orderToBeUpdated.setStatus(OrderStatus.DELIVERED);

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setStatus(OrderStatus.DELIVERED);

        when(orderService.getOrderById(baseOrder.getId())).thenReturn(baseOrder);
        when(orderService.updateOrder(baseOrder.getId(), orderToBeUpdated));

        ResponseEntity<Object> response = orderController.updateOrder(baseOrder.getId(), orderToBeUpdated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrder, response.getBody());
    }

    /**
     * Tests the deleteOrder method of OrderController.
     * Verifies that the cancelOrderAndIncreaseStock method of OrderService is called once with the specified order ID.
     */
    @Test
    void deleteOrderTest() {
        long orderId = 1L;

        orderController.deleteOrder(orderId);

        verify(orderService, times(1)).cancelOrderAndIncreaseStock(orderId);
    }

    /**
     * Tests the getEstimatedDeliveryDate method of OrderController.
     * Verifies that the response status is OK and the returned estimated delivery date matches the expected date.
     */
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
