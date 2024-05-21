package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticservices;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import org.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ElasticOrderService {

    private final OrderRepository orderRepository;
    private final OrderDocumentRepository orderDocumentRepository;

    public ElasticOrderService(OrderRepository orderRepository, OrderDocumentRepository orderDocumentRepository) {
        this.orderRepository = orderRepository;
        this.orderDocumentRepository = orderDocumentRepository;
    }

    @Transactional
    public void saveOrderToElasticsearch(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        OrderDocument orderDocument = new OrderDocument();

        orderDocument.setId(order.getId().toString());
        orderDocument.setCustomerId(order.getCustomer().getId());
        orderDocument.setStatus(order.getStatus().toString());
        orderDocument.setOrderDate(order.getOrderDate());
        orderDocument.setEstimatedDeliveryDate(order.getEstimatedDeliveryDate());

        orderDocumentRepository.save(orderDocument);
    }
}
