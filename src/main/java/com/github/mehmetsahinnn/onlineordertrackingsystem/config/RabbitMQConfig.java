package com.github.mehmetsahinnn.onlineordertrackingsystem.config;


import com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners.OrderListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


@Configuration
public class RabbitMQConfig {

    @Value("${sr.rabbit.queue.name}")
    private String queueName;

    @Value("${sr.rabbit.routing.name}")
    private String routingName;

    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQConfig( RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.github.mehmetsahinnn.onlineordertrackingsystem.models");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(OrderListener orderListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(orderListener, "handleMessage");
        adapter.setMessageConverter(jsonMessageConverter());
        return adapter;
    }


    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public Queue queue(){
        return new Queue(queueName,true);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchangeName);

    }
    @Bean
    public Binding binding(final Queue queue, final  DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(routingName);
    }

    @Bean
    @Lazy
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}