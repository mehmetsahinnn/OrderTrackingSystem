package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.InsufficientStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.KeycloakClient;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.ResponseHandler;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * The OrderService class provides services for managing orders.
 * It includes services for placing, retrieving, updating, and deleting orders.
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final KeycloakClient keycloakClient;


    /**
     * Constructs a new OrderService with the specified OrderRepository and ProductService.
     *
     * @param orderRepository the OrderRepository to be used by the OrderService
     * @param productService  the ProductService to be used by the OrderService
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService, KeycloakClient keycloakClient) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.keycloakClient = keycloakClient;
    }

    /**
     * Places a new order. If the product's stock is insufficient or not available, an exception is thrown.
     * If the order is successfully placed, the product's stock is updated and the order's status is set to CONFIRMED.
     * The order is then saved in the repository and returned.
     *
     * @param order the order to be placed
     * @return the placed order
     * @throws IllegalArgumentException if the product's stock is insufficient or not available
     */
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> placeOrder(Order order) {
        try {
            order.getOrderItems().forEach(this::validateAndUpdateStock);

            order.setStatus(OrderStatus.CONFIRMED);
            Order savedOrder = orderRepository.save(order);

            return ResponseHandler.generateResponse("Order placed successfully.", HttpStatus.CREATED, savedOrder);
        } catch (RuntimeException ex) {
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    private void validateAndUpdateStock(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        int numberInStock = Optional.ofNullable(product.getNumberInStock()).orElse(0);

        if (orderItem.getQuantity() > numberInStock) {
            logger.error("Error occurred while placing the order: Insufficient stock for product id: {}", product.getId());
            throw new InsufficientStockException("Insufficient stock for product id: " + product.getId());
        }

        productService.updateStock(product.getId(), -orderItem.getQuantity());
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the retrieved order
     */
    public Order getOrderById(Long id) {
        try {
            return orderRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving the order with id: " + id, e);
        }
    }

    /**
     * Retrieves all orders.
     *
     * @return the list of all orders
     */
    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving all orders", e);
        }
    }

    /**
     * Deletes an order.
     *
     * @param id the ID of the order to delete
     */
    public void deleteOrder(Long id) {
        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting the order with id: " + id, e);
        }
    }

    /**
     * Updates an order.
     *
     * @param id           the ID of the order to update
     * @param newOrderData the new order data
     * @return the updated order
     */
    public ResponseEntity<Object> updateOrder(Long id, Order newOrderData) {
        try {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("No Order found with id: " + id));

            existingOrder.setOrderItems(newOrderData.getOrderItems());

            OrderStatus newStatus = getOrderStatus(newOrderData, existingOrder);

            existingOrder.setStatus(newStatus);
            Order updatedOrder = orderRepository.save(existingOrder);
            return ResponseHandler.generateResponse("Order updated successfully.", HttpStatus.OK, updatedOrder);
        } catch (NoSuchElementException ex) {
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    private static OrderStatus getOrderStatus(Order newOrderData, Order existingOrder) {
        OrderStatus newStatus = newOrderData.getStatus();
        OrderStatus currentStatus = existingOrder.getStatus();

        if (newStatus == OrderStatus.SHIPPED && currentStatus != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Order must be CONFIRMED before it can be SHIPPED");
        } else if (newStatus == OrderStatus.DELIVERED && currentStatus != OrderStatus.SHIPPED) {
            throw new RuntimeException("Order must be SHIPPED before it can be DELIVERED");
        } else if (newStatus == OrderStatus.CANCELLED && currentStatus == OrderStatus.DELIVERED) {
            throw new RuntimeException("DELIVERED orders cannot be CANCELLED");
        } else if (newStatus == OrderStatus.CANCELLED && currentStatus == OrderStatus.CONFIRMED) {
            // Allow orders to be cancelled if they are in the CONFIRMED state
        }
        return newStatus;
    }

    /**
     * Cancels the order identified by the given order ID, sets its status to CANCELLED,
     * and increases the stock of the associated product by the quantity of the canceled order.
     *
     * @param orderId the ID of the order to be canceled
     * @throws RuntimeException if no order is found with the given order ID
     */
    public void cancelOrderAndIncreaseStock(Long orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                ResponseHandler.generateResponse("Order not found with id: " + orderId, HttpStatus.NOT_FOUND, null);
                return;

            }

            order.setStatus(OrderStatus.CANCELLED);

            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                Integer currentStock = product.getNumberInStock();
                product.setNumberInStock(currentStock + orderItem.getQuantity());

                productService.updateProduct(product.getId(), product);
            }

            ResponseHandler.generateResponse("Order cancelled and stock increased successfully.", HttpStatus.OK, null);
        } catch (Exception e) {
            ResponseHandler.generateResponse("Error occurred while cancelling the order and increasing the stock.", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


}