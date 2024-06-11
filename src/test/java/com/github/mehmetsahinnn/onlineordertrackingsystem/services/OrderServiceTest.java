package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.InsufficientStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.KeycloakClient;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticservices.ElasticOrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the OrderService class.
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDocumentRepository orderDocumentRepository;
    @InjectMocks
    private ElasticOrderService elasticOrderService;
    @Mock
    private ProductService productService;

    /**
     * Executes before each test method.
     */
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void placeOrder() {
        Order order = new Order();
        order.setOrderItems(Collections.singletonList(new OrderItem(null, order, new Product(1L, "Product", "Description", "Category", 10.0, 10), 1, 10.0)));
        OrderService orderService = new OrderService(mock(OrderRepository.class), mock(ProductService.class), mock(KeycloakClient.class));
        ResponseEntity<Object> response = orderService.placeOrder(order);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    /**
     * Tests the getAllOrders method of OrderService.
     * Verifies that all orders are retrieved successfully.
     */
    @Test
    public void testGetAllOrders() {
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getAllOrders();

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void test_existing_order_found_by_id() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        OrderRepository mockOrderRepository = mock(OrderRepository.class);
        when(mockOrderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        OrderService orderService = new OrderService(mockOrderRepository, mock(ProductService.class), mock(KeycloakClient.class));
        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(existingOrder.getId(), result.getId());
    }


    @Test
    public void test_order_items_iterated_to_check_and_update_stock() {
        Product product = new Product(1L, "Product", "Description", "Category", 20.0, 20);
        List<OrderItem> items = Arrays.asList(
                new OrderItem(null, null, product, 5, 50.0),
                new OrderItem(null, null, product, 5, 50.0)
        );
        Order order = new Order();
        order.setOrderItems(items);
        ProductService productServiceMock = mock(ProductService.class);

        OrderRepository orderRepositoryMock = mock(OrderRepository.class);
        when(orderRepositoryMock.save(any(Order.class))).thenReturn(order);

        KeycloakClient keycloakClientMock = mock(KeycloakClient.class);

        OrderService orderService = new OrderService(orderRepositoryMock, productServiceMock, keycloakClientMock);

        ResponseEntity<Object> response = orderService.placeOrder(order);

        verify(productServiceMock, times(2)).updateStock(eq(1L), eq(-5));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_order_cancelled_and_stock_increased() {
        // Arrange
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(OrderStatus.CONFIRMED);
        OrderItem mockOrderItem = new OrderItem();
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setNumberInStock(10);
        mockOrderItem.setProduct(mockProduct);
        mockOrderItem.setQuantity(5);
        mockOrder.setOrderItems(Collections.singletonList(mockOrderItem));

        OrderRepository mockOrderRepository = mock(OrderRepository.class);
        ProductService mockProductService = mock(ProductService.class);
        KeycloakClient mockKeycloakClient = mock(KeycloakClient.class);

        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(mockProductService.updateProduct(anyLong(), any(Product.class))).thenReturn(mockProduct);

        OrderService orderService = new OrderService(mockOrderRepository, mockProductService, mockKeycloakClient);

        // Act
        orderService.cancelOrderAndIncreaseStock(orderId);

        // Assert
        assertEquals(OrderStatus.CANCELLED, mockOrder.getStatus());
        assertEquals(15, mockProduct.getNumberInStock().intValue());
    }

    /**
     * Tests the deleteOrder method of OrderService.
     * Verifies that the order is deleted successfully.
     */
    @Test
    public void delete_existing_order_by_id() {
        OrderRepository mockOrderRepository = mock(OrderRepository.class);
        doNothing().when(mockOrderRepository).deleteById(1L);
        OrderService orderService = new OrderService(mockOrderRepository, mock(ProductService.class), mock(KeycloakClient.class));
        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
    }

    @Test
    public void testFilterAndMapOrders() {
        List<OrderDocument> orders = Arrays.asList(
                new OrderDocument("1", 1L, OrderStatus.CONFIRMED, LocalDate.of(2024, 6, 1), 0L),
                new OrderDocument("2", 2L, OrderStatus.CONFIRMED, LocalDate.of(2024, 8, 5), 0L),
                new OrderDocument("3", 1L, OrderStatus.CONFIRMED, LocalDate.of(2024, 4, 9), 0L),
                new OrderDocument("4", 3L, OrderStatus.CONFIRMED, LocalDate.of(2024, 7, 12), 0L)
        );

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 30);

        when(orderDocumentRepository.findOrdersByDateBetween(startDate, endDate)).thenReturn(orders);

        Map<Long, List<OrderDocument>> result = elasticOrderService.filterOrderByDate(startDate, endDate);

        assertEquals(1, result.size());
        assertEquals(1, result.get(3L).size());
    }
}