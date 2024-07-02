package com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.InsufficientStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.ProductRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class OrderListener {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderListener(OrderRepository orderRepository,ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = "order-queue")
    public void handleMessage(Order order) {
        System.out.println("Received message: " + order);
        try {
            order.setStatus(OrderStatus.CONFIRMED);
            order.setEstimatedDeliveryDate(LocalDate.now().plusDays(5));
            orderRepository.save(order);
            order.getOrderItems().forEach(this::updateStock);
        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
        }
    }


    private void updateStock(OrderItem orderItem) {
        Product product = productRepository.findById(orderItem.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderItem.getProduct().getId()));

        int currentStock = product.getNumberInStock() != null ? product.getNumberInStock() : 0;
        int updatedStock = currentStock - orderItem.getQuantity();

        if (updatedStock < 0) {
            throw new InsufficientStockException("Insufficient stock for product id: " + product.getId());
        }

        product.setNumberInStock(updatedStock);
        productRepository.save(product);
    }

}
