package com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.InsufficientStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class OrderListener {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Autowired
    public OrderListener(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @RabbitListener(queues = "order-queue")
    public void handleMessage(Order order) {
        System.out.println("Received message: " + order);

        try {
            order.getOrderItems().forEach(this::validateAndUpdateStock);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setEstimatedDeliveryDate(LocalDate.now().plusDays(5));

            orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
        }
    }

    private void validateAndUpdateStock(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        int numberInStock = Optional.ofNullable(product.getNumberInStock()).orElse(0);

        if (orderItem.getQuantity() > numberInStock) {
            throw new InsufficientStockException("Insufficient stock for product id: " + product.getId());
        }

        productService.updateStock(product.getId(), -orderItem.getQuantity());
    }
}
