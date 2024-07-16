package com.github.mehmetsahinnn.onlineordertrackingsystem.tdd;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners.OrderListener;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticservices.ElasticOrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.ProductRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the OrderService class.
 */
@ExtendWith(MockitoExtension.class)
public class TDDOrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDocumentRepository orderDocumentRepository;
    @InjectMocks
    private ElasticOrderService elasticOrderService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderListener orderListener;


    /**
     * Executes before each test method.
     */
    @BeforeEach
    public void init() {
//        MockitoAnnotations.openMocks(this);
        System.out.println(" ");
    }

    @Test
    public void testFilterOrderByDate() {
        List<OrderDocument> orders = List.of(
                new OrderDocument("1", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 10, 0), LocalDateTime.of(2024, 7, 1, 10, 0))
        );

        LocalDateTime startDate = LocalDateTime.of(2024, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 7, 1, 23, 59, 59);

        when(orderDocumentRepository.findOrdersByDateBetween(startDate, endDate)).thenReturn(orders);

        Map<Long, List<OrderDocument>> result = elasticOrderService.filterOrderByDate(startDate, endDate);

        assertEquals(1, result.size());
        assertEquals(1, result.get(1L).size());
        assertEquals("1", result.get(1L).get(0).getId());
    }

    @Test
    public void testHandleMultipleBySameUser() {
        List<OrderDocument> orders = List.of(
                new OrderDocument("1", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 6, 1, 10, 0), LocalDateTime.of(2024, 6, 1, 10, 0)),
                new OrderDocument("2", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 8, 5, 10, 0), LocalDateTime.of(2024, 8, 5, 10, 0)),
                new OrderDocument("3", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 4, 9, 10, 0), LocalDateTime.of(2024, 4, 9, 10, 0)),
                new OrderDocument("4", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 12, 10, 0), LocalDateTime.of(2024, 7, 12, 10, 0))
        );

        LocalDateTime startDate = LocalDateTime.of(2024, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 7, 20, 23, 59, 59);
        String username = "mehmet";

        when(orderDocumentRepository.findUserByUsername(username)).thenReturn(orders);

        Map<Long, List<OrderDocument>> result = elasticOrderService.filterOrderByUser(username, startDate, endDate);

        assertEquals(1, result.size());
        assertEquals(1, result.get(1L).size());
        assertEquals("4", result.get(1L).get(0).getId());
    }

    @Test
    public void testEmptyDateMap() {
        List<OrderDocument> orders = Arrays.asList(
                new OrderDocument("1", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 10, 5), LocalDateTime.of(2024, 7, 1, 10, 0)),
                new OrderDocument("2", "mehmet", 2L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 6, 7, 2, 12), LocalDateTime.of(2024, 7, 1, 10, 0)),
                new OrderDocument("3", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 9, 43), LocalDateTime.of(2024, 7, 1, 10, 0)),
                new OrderDocument("4", "mehmet", 3L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 6, 18), LocalDateTime.of(2024, 7, 1, 10, 0))
        );

        LocalDateTime startDate = LocalDateTime.of(1024, 7, 1, 2, 2, 1, 1);
        LocalDateTime endDate = LocalDateTime.of(1026, 7, 20, 1, 1, 1, 1);


        when(orderDocumentRepository.findOrdersByDateBetween(startDate, endDate)).thenReturn(Collections.emptyList());

        Map<Long, List<OrderDocument>> result = elasticOrderService.filterOrderByDate(startDate, endDate);


        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterOrderByUserWithSameDayOrderAndDelivery() {
        List<OrderDocument> orders = List.of(
                new OrderDocument("1", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 10, 0), LocalDateTime.of(2024, 7, 1, 10, 0)),
                new OrderDocument("2", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 12, 0), LocalDateTime.of(2024, 7, 1, 12, 0))
        );

        LocalDateTime startDate = LocalDateTime.of(2024, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 7, 1, 23, 59, 59);
        String username = "mehmet";

        when(orderDocumentRepository.findUserByUsername(username)).thenReturn(orders);

        Map<Long, List<OrderDocument>> result = elasticOrderService.filterOrderByUser(username, startDate, endDate);

        assertEquals(1, result.size());
        assertEquals(2, result.get(1L).size());
        assertEquals(Long.valueOf("1"), result.get(1L).get(0).getCustomerId());
        assertEquals(Long.valueOf("1"), result.get(1L).get(1).getCustomerId());
    }
}


