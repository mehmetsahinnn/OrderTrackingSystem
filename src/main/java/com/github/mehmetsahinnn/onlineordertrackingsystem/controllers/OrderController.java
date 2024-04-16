package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.services.OrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.ResponseHandler;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 * The OrderController class provides REST ful API endpoints for managing orders.
 * It includes endpoints for placing, retrieving, updating, and deleting orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    /**
     * Constructs a new OrderController with the specified OrderService.
     *
     * @param orderService the OrderService to be used by the OrderController
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Places a new order.
     *
     * @param order the order to be placed
     * @return a ResponseEntity containing the placed order and the HTTP status
     */
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        try {
            Order placedOrder = orderService.placeOrder(order);
            return new ResponseEntity<>(placedOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves all orders.
     *
     * @return a ResponseEntity containing the list of all orders and the HTTP status
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving orders", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing the retrieved order and the HTTP status
     */
    @GetMapping("/track/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseHandler.generateResponse("Can't find the item", HttpStatus.NOT_FOUND, null);
            }
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an order.
     *
     * @param id the ID of the order to update
     * @param newOrderData the new order data
     * @return a ResponseEntity containing the updated order and the HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable Long id, @RequestBody Order newOrderData) {
        try {
            Order updatedOrder = orderService.updateOrder(id, newOrderData);
            if (updatedOrder == null) {
                return ResponseHandler.generateResponse("Can't find the item", HttpStatus.NOT_FOUND, null);
            }
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Cancels an order by its ID and increases the product stock by the quantity of the order.
     *
     * @param id the ID of the order to cancel
     * @return a ResponseEntity containing the HTTP status
     */
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrderAndIncreaseStock(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the estimated delivery date of an order by its ID.
     *
     * @param id the ID of the order
     * @return a ResponseEntity containing the estimated delivery date of the order and the HTTP status
     */
    @GetMapping("/track/{id}/estimatedDeliveryDate")
    public ResponseEntity<?> getEstimatedDeliveryDate(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(order.getEstimatedDeliveryDate(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}