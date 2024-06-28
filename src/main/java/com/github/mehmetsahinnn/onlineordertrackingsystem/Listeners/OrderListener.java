package com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class OrderListener {

    private final OrderRepository orderRepository;

    public OrderListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = "order-queue")
    public void handleMessage(Order order) {
        System.out.println("Received message: " + order);
        orderRepository.save(order);
    }
}