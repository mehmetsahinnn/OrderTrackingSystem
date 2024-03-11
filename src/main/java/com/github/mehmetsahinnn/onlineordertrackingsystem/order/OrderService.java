package com.github.mehmetsahinnn.onlineordertrackingsystem.order;

import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The OrderService class provides services for managing orders.
 * It includes services for placing, retrieving, updating, and deleting orders.
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    /**
     * Constructs a new OrderService with the specified OrderRepository and ProductService.
     *
     * @param orderRepository the OrderRepository to be used by the OrderService
     * @param productService the ProductService to be used by the OrderService
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
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
    public Order placeOrder(Order order) {
        Product product = order.getProduct();
        Integer numberInStock = product.getNumberInStock();

        if (numberInStock == null || order.getQuantity() > numberInStock){
            logger.error("Error occurred while placing the order: Insufficient stock or stock information not available.");
            throw new IllegalArgumentException("Insufficient stock or stock information not available.");
        }

        productService.updateStock(product.getId(), numberInStock - order.getQuantity());
        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
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
     * @param id the ID of the order to update
     * @param newOrderData the new order data
     * @return the updated order
     */
    public Order updateOrder(Long id, Order newOrderData) {
        try {
            Order existingOrder = orderRepository.findById(id).orElse(null);
            if (existingOrder != null) {
                existingOrder.setProduct(newOrderData.getProduct());
                existingOrder.setQuantity(newOrderData.getQuantity());

                OrderStatus newStatus = getOrderStatus(newOrderData, existingOrder);

                existingOrder.setStatus(newStatus);
                orderRepository.save(existingOrder);
            }
            return existingOrder;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating the order with id: " + id, e);
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
        }
        return newStatus;
    }

    /**
     * Cancels the order identified by the given order ID and increases the stock of the associated product by the quantity of the canceled order.
     *
     * @param orderId the ID of the order to be canceled
     * @throws RuntimeException if no order is found with the given order ID
     */
    public void cancelOrderAndIncreaseStock(Long orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }

        Product product = order.getProduct();
        Integer currentStock = product.getNumberInStock();
        product.setNumberInStock(currentStock + order.getQuantity());

        deleteOrder(orderId);
    }
}