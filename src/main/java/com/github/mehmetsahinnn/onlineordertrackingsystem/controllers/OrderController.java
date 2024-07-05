package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticservices.ElasticOrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.OrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


/**
 * The OrderController class provides REST ful API endpoints for managing orders.
 * It includes endpoints for placing, retrieving, updating, and deleting orders.
 */
@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController extends BaseController{

    private final OrderService orderService;
    private final ElasticOrderService elasticOrderService;

    /**
     * Constructs a new OrderController with the specified OrderService.
     *
     * @param orderService the OrderService to be used by the OrderController
     */
    @Autowired
    public OrderController(OrderService orderService, ElasticOrderService elasticOrderService) {
        this.orderService = orderService;
        this.elasticOrderService = elasticOrderService;
    }

    /**
     * Places a new order.
     *
     * @param order the order to be placed
     * @return a ResponseEntity containing the placed order and the HTTP status
     */
    @PostMapping
    public ResponseEntity<Object> placeOrder(@RequestBody Order order) {
        return handleRequest(() -> orderService.placeOrder(order), "Order placed successfully");
    }

    /**
     * Retrieves all orders.
     *
     * @return a ResponseEntity containing the list of all orders and the HTTP status
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return handleRequest(orderService::getAllOrders, "Fetching all orders");
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing the retrieved order and the HTTP status
     */
    @GetMapping("/track/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
        return handleRequest(() -> orderService.getOrderById(id), "Fetching order by id");
    }

    /**
     * Updates an order.
     *
     * @param id           the ID of the order to update
     * @param newOrderData the new order data
     * @return a ResponseEntity containing the updated order and the HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable Long id, @RequestBody Order newOrderData) {
        return handleRequest(() -> orderService.updateOrder(id, newOrderData), "Updated order successfully");
    }

    /**
     * Cancels an order by its ID and increases the product stock by the quantity of the order.
     *
     * @param id the ID of the order to cancel
     * @return a ResponseEntity containing the HTTP status
     */
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        return handleRequest(() -> {
            orderService.cancelOrderAndIncreaseStock(id);
            return null;
        }, "Deleted order successfully");
    }

    /**
     * Retrieves the estimated delivery date of an order by its ID.
     *
     * @param id the ID of the order
     * @return a ResponseEntity containing the estimated delivery date of the order and the HTTP status
     */
    @GetMapping("/track/{id}/estimatedDeliveryDate")
    public ResponseEntity<?> getEstimatedDeliveryDate(@PathVariable Long id) {
        return handleRequest(() -> orderService.getOrderById(id).getEstimatedDeliveryDate(), "Fetching estimated delivery date");
    }

    @PostMapping("/elastic/orders")
    public OrderDocument createOrder(@RequestBody OrderDocument orderDocument) {
        return elasticOrderService.saveOrderDocument(orderDocument);
    }

    @GetMapping("/elastic/orders")
    public List<OrderDocument> getOrdersByStatus(@RequestParam String status) {
        return elasticOrderService.findByStatus(status);
    }

    @GetMapping("/{orderTrackId}")
    public Order getOrderByTrackId(@PathVariable UUID orderTrackId) {
        return orderService.getOrderByTrackId(orderTrackId);
    }
}