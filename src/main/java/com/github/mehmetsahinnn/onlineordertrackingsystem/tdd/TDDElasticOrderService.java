
package com.github.mehmetsahinnn.onlineordertrackingsystem.tdd;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TDDElasticOrderService {
    private final OrderDocumentRepository orderDocumentRepository;

    public TDDElasticOrderService(OrderDocumentRepository orderDocumentRepository) {
        this.orderDocumentRepository = Objects.requireNonNull(orderDocumentRepository, "OrderDocumentRepository cannot be null");
    }

    /**
     * Filters orders by the given date range and groups them by customer ID.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return a map where the key is the customer ID and the value is a list of orders for that customer
     */
    public Map<Long, List<OrderDocument>> filterOrdersByDate(LocalDateTime startDate, LocalDateTime endDate) {
        Objects.requireNonNull(startDate, "Start date cannot be null");
        Objects.requireNonNull(endDate, "End date cannot be null");

        List<OrderDocument> orders = orderDocumentRepository.findOrdersByDateBetween(startDate, endDate);
        return orders.stream().collect(Collectors.groupingBy(OrderDocument::getCustomerId));
    }

    /**
     * Filters orders by the given username and date range, and groups them by customer ID.
     *
     * @param username  the username to filter by
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return a map where the key is the customer ID and the value is a list of orders for that customer
     */
    public Map<Long, List<OrderDocument>> filterOrdersByUser(String username, LocalDateTime startDate, LocalDateTime endDate) {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(startDate, "Start date cannot be null");
        Objects.requireNonNull(endDate, "End date cannot be null");

        List<OrderDocument> orders = orderDocumentRepository.findUserByUsername(username);
        return orders.stream()
                .filter(order -> !order.getOrderDate().isBefore(startDate) && !order.getOrderDate().isAfter(endDate))
                .collect(Collectors.groupingBy(OrderDocument::getCustomerId));
    }
}