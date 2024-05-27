package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticservices;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import org.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ElasticOrderService {

    private final OrderDocumentRepository orderDocumentRepository;

    public ElasticOrderService(OrderDocumentRepository orderDocumentRepository) {

        this.orderDocumentRepository = orderDocumentRepository;
    }

    public OrderDocument saveOrderDocument(OrderDocument orderDocument) {
        return orderDocumentRepository.save(orderDocument);
    }

    public List<OrderDocument> findByStatus(String status) {
        return orderDocumentRepository.findByStatus(status);
    }
}
