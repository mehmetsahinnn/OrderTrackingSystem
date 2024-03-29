package com.github.mehmetsahinnn.onlineordertrackingsystem.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CustomerService class.
 */
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    /**
     * Initializes Mockito annotations before each test method.
     */
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the saveCustomer method of CustomerService.
     * Verifies that the saved customer matches the input customer.
     */
    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer(1L, "msa", "hin", "password", "john.doe@example.com", false);

        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.saveCustomer(customer);

        assertEquals(customer.getName(), savedCustomer.getName());
        assertEquals(customer.getPassword(), savedCustomer.getPassword());
    }

    /**
     * Tests the findAll method of CustomerService.
     * Verifies that the list of customers returned matches the expected list.
     */
    @Test
    public void testFindAllCustomer() {
        Customer customer = new Customer(1L, "msa", "hin", "password", "john.doe@example.com", false);
        List<Customer> expectedCustomers = Collections.singletonList(customer);

        when(customerRepository.findAll()).thenReturn(expectedCustomers);

        List<Customer> actualCustomers = customerService.findAll();

        assertEquals(expectedCustomers, actualCustomers);
    }

    /**
     * Tests the findByEmail method of CustomerService.
     * Verifies that the customer returned matches the expected customer.
     */
    @Test
    public void testFindCustomerByEmail() {
        Customer expectedCustomer = new Customer(1L, "msa", "hin", "password", "john.doe@example.com", false);

        when(customerRepository.findByEmail(anyString())).thenReturn(expectedCustomer);

        Customer actualCustomer = customerService.findByEmail("john.doe@example.com");

        assertEquals(expectedCustomer, actualCustomer);
    }

    /**
     * Tests the getCurrentUser method of CustomerService.
     * Verifies that the current customer returned matches the expected customer.
     */
    @Test
    public void testGetCurrentCustomer() {
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(userDetails.getUsername()).thenReturn("john.doe@example.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Customer expectedCustomer = new Customer(1L, "msa", "hin", "password", "john.doe@example.com", false);
        when(customerRepository.findByEmail(anyString())).thenReturn(expectedCustomer);

        Customer actualCustomer = customerService.getCurrentUser();

        assertEquals(expectedCustomer, actualCustomer);
    }

    /**
     * Tests the deleteCustomerById method of CustomerService.
     * Verifies that the delete operation is invoked once for the specified customer ID.
     */
    @Test
    public void testDeleteProduct() {
        Long customerId = 1L;

        doNothing().when(customerRepository).deleteById(customerId);

        customerService.deleteCustomerById(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }


}