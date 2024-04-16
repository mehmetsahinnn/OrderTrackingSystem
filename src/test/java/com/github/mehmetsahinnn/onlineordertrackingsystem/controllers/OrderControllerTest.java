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

    /**
     * Tests the getAllOrders method of OrderController.
     * Verifies that the response status is OK and the returned list of orders matches the expected list.
     */
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

    /**
     * Tests the getOrderById method of OrderController.
     * Verifies that the response status is OK and the returned order matches the expected order.
     */
    @Test
    public void getOrderByIdTest(){
        Order order = new Order();
        order.setId(1L);
        order.setQuantity(10);
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
