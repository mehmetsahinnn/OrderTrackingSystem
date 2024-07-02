package com.github.mehmetsahinnn.onlineordertrackingsystem.producers;

import com.github.mehmetsahinnn.onlineordertrackingsystem.config.RabbitMQConfig;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Component
public class OrderProducer {

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