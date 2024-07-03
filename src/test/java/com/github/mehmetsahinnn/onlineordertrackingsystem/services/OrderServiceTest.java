package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions.InsufficientStockException;
import com.github.mehmetsahinnn.onlineordertrackingsystem.Listeners.OrderListener;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.KeycloakClient;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos.OrderDocumentRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticservices.ElasticOrderService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.producers.OrderProducer;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.OrderRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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
    public void placeOrder() {
        Order order = new Order();
        order.setOrderItems(Collections.singletonList(new OrderItem(null, order, new Product(1L, "Product", "Description", "Category", 10.0, 10), 1, 10.0)));
        OrderService orderService = new OrderService(mock(OrderRepository.class), mock(ProductService.class), mock(OrderProducer.class) ,mock(KeycloakClient.class), mock(ProductRepository.class));
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

        OrderService orderService = new OrderService(mockOrderRepository, mock(ProductService.class), mock(OrderProducer.class), mock(KeycloakClient.class), mock(ProductRepository.class));
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

        OrderService orderService = new OrderService(orderRepositoryMock, productServiceMock, mock(OrderProducer.class), keycloakClientMock, mock(ProductRepository.class));

        ResponseEntity<Object> response = orderService.placeOrder(order);

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

        OrderService orderService = new OrderService(mockOrderRepository, mockProductService, mock(OrderProducer.class), mockKeycloakClient, mock(ProductRepository.class));

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
        OrderService orderService = new OrderService(mockOrderRepository, mock(ProductService.class), mock(OrderProducer.class), mock(KeycloakClient.class), mock(ProductRepository.class));
        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
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
    public void testEmptyDateMap(){
        List<OrderDocument> orders = Arrays.asList(
                new OrderDocument("1", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 10, 5), LocalDateTime.of(2024, 7, 1, 10, 0)),
                new OrderDocument("2", "mehmet", 2L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 6, 7, 2, 12), LocalDateTime.of(2024, 7, 1, 10, 0)),
                new OrderDocument("3", "mehmet", 1L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 9, 43), LocalDateTime.of(2024, 7, 1, 10, 0)),
                new OrderDocument("4", "mehmet", 3L, OrderStatus.CONFIRMED, LocalDateTime.of(2024, 7, 1, 6, 18), LocalDateTime.of(2024, 7, 1, 10, 0))
        );

        LocalDateTime startDate = LocalDateTime.of(1024, 7, 1,2,2,1,1);
        LocalDateTime endDate = LocalDateTime.of(1026, 7, 20,1,1,1,1);


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
        assertEquals("1", result.get(1L).get(0).getId());
        assertEquals("2", result.get(1L).get(1).getId());
    }

    @Test
    public void insufficientStockException() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description", "Category", 10.0, 2);
        OrderItem orderItem = new OrderItem(1L, null, product, 5, 50.0);
        Order order = new Order();
        order.setOrderItems(List.of(orderItem));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doThrow(new InsufficientStockException("Insufficient stock for product id: 1")).when(orderRepository).save(any(Order.class));

        // Act & Assert
        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () -> {
            orderService.placeOrder(order);
        });

        assertEquals("Insufficient stock for product id: 1", exception.getMessage());

        verify(orderRepository, never()).save(any(Order.class));
        verify(productRepository, never()).save(any(Product.class));
    }


    @Test
    public void productNotFoundException() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description", "Category", 10.0, 10);
        OrderItem orderItem = new OrderItem(1L, null, product, 5, 50.0);
        Order order = new Order();
        order.setOrderItems(List.of(orderItem));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderListener.handleMessage(order);
        });

        assertEquals("Product not found with id: 1", exception.getMessage());

        verify(orderRepository, never()).save(any(Order.class));
    }

}