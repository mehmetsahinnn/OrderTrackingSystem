package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticservices;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return orderDocumentRepository.searchByStatusWithFuzziness(status);
    }

    public Map<Long, List<OrderDocument>> filterOrderByDate(LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderDocument> orders = orderDocumentRepository.findOrdersByDateBetween(startDate, endDate);

        return orders.stream()
                .collect(Collectors.groupingBy(OrderDocument::getCustomerId));
    }

    public Map<Long, List<OrderDocument>> filterOrderByUser(String username, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderDocument> userOrders = orderDocumentRepository.findUserByUsername(username);

        return userOrders.stream()
                .filter(order -> (order.getOrderDate().isAfter(startDate) && order.getOrderDate().isBefore(endDate)) ||
                        order.getOrderDate().isEqual(startDate) || order.getOrderDate().isEqual(endDate))
                .collect(Collectors.groupingBy(OrderDocument::getCustomerId));
    }
}
