package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.OrderItemService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/orderItems")
@Log4j2
public class OrderItemController extends BaseController{

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrderItems() {
        return handleRequest(orderItemService::getAllOrderItems, "orders");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderItemById(@PathVariable Long id) {
        return handleRequest(() -> orderItemService.getOrderItemById(id), "order");
    }

    @PostMapping
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItem orderItem) {
        return handleRequest(() -> orderItemService.createOrderItem(orderItem), "created_order_item");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Long id, @RequestBody OrderItem orderItemDetails) {
        return handleRequest(() -> orderItemService.updateOrderItem(id, orderItemDetails), "updated_order_item");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long id) {
        return handleRequest(() -> {
            orderItemService.deleteOrderItem(id);
            return null;
        }, "deleted_order_item");
    }
}
