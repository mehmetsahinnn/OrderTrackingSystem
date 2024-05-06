package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.config.ResponseHandler;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public ResponseEntity<Object> getOrderItemById(Long id) {
        try {
            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("No OrderItem found with id: " + id));
            return ResponseHandler.generateResponse("Order item found successfully.", HttpStatus.OK, orderItem);
        } catch (NoSuchElementException ex) {
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    public ResponseEntity<Object> updateOrderItem(Long id, OrderItem orderItemDetails) {
        try {
            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("No OrderItem found with id: " + id));
            orderItem.setProduct(orderItemDetails.getProduct());
            orderItem.setQuantity(orderItemDetails.getQuantity());
            orderItem.setTotalPrice(orderItemDetails.getTotalPrice());
            OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
            return ResponseHandler.generateResponse("Order item updated successfully.", HttpStatus.OK, updatedOrderItem);
        } catch (NoSuchElementException ex) {
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    public ResponseEntity<Object> deleteOrderItem(Long id) {
        try {
            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("No OrderItem found with id: " + id));
            orderItemRepository.delete(orderItem);
            return ResponseHandler.generateResponse("Order item deleted successfully.", HttpStatus.OK, null);
        } catch (NoSuchElementException ex) {
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    public ResponseEntity<Object> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        if (orderItems.isEmpty()) {
            return ResponseHandler.generateResponse("No order items found.", HttpStatus.NOT_FOUND, null);
        } else {
            return ResponseHandler.generateResponse("Order items retrieved successfully.", HttpStatus.OK, orderItems);
        }
    }

    public ResponseEntity<Object> createOrderItem(OrderItem orderItem) {
        try {
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            return ResponseHandler.generateResponse("Order item created successfully.", HttpStatus.CREATED, savedOrderItem);
        } catch (Exception ex) {
            return ResponseHandler.generateResponse("Error creating order item.", HttpStatus.BAD_REQUEST, null);
        }
    }
}