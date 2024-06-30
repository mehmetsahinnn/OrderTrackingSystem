package com.github.mehmetsahinnn.onlineordertrackingsystem.producers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.config.RabbitMQConfig;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.AccountStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


@Service
@Component
public class OrderProducer {

    Order order = new Order();

    @Value("${sr.rabbit.routing.name}")
    private String routingName;

    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    public OrderProducer(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    public void sendToQueue(Order order) {
        System.out.println("Order ID : " + order.getOrderTrackId());
        rabbitTemplate.setMessageConverter(rabbitMQConfig.jsonMessageConverter());
        rabbitTemplate.convertAndSend(exchangeName, routingName, order);
    }


}