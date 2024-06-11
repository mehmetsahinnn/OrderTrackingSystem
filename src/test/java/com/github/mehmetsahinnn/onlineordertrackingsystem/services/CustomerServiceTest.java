//package com.github.mehmetsahinnn.onlineordertrackingsystem.services;
//
//import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.AccountStatus;
//import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
//import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.CustomerRepository;
//import com.github.mehmetsahinnn.onlineordertrackingsystem.security.PCrypt;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.security.Key;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for the CustomerService class.
// */
//class CustomerServiceTest {
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @InjectMocks
//    private CustomerService customerService;
//
//    @InjectMocks
//    private PCrypt pCrypt;
//
//    @InjectMocks
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    /**
//     * Initializes Mockito annotations before each test method.
//     */
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    /**
//     * Tests the saveCustomer method of CustomerService.
//     * Verifies that the saved customer matches the input customer.
//     */
//    @Test
//    public void testSaveCustomer() {
//        Customer customer = new Customer(1L, "msa", "hin", "123 Oak Street","password", "john.doe@example.com", AccountStatus.USER);
//
//        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);
//
//        Customer savedCustomer = customerService.saveCustomer(customer);
//        verify(customerRepository).save(customer);
//
//        assertEquals(customer.getName(), savedCustomer.getName());
//        assertEquals(customer.getPassword(), savedCustomer.getPassword());
//    }
//
//    /**
//     * Tests the findAll method of CustomerService.
//     * Verifies that the list of customers returned matches the expected list.
//     */
//    @Test
//    public void testFindAllCustomer() {
//        Customer customer = new Customer(1L, "msa", "hin", "123 Oak Street","password", "john.doe@example.com", AccountStatus.USER);
//        List<Customer> expectedCustomers = Collections.singletonList(customer);
//
//        when(customerRepository.findAll()).thenReturn(expectedCustomers);
//
//        List<Customer> actualCustomers = customerService.findAll();
//
//        assertEquals(expectedCustomers, actualCustomers);
//    }
//
//    /**
//     * Tests the findByEmail method of CustomerService.
//     * Verifies that the customer returned matches the expected customer.
//     */
//    @Test
//    public void testFindCustomerByEmail() {
//        Customer expectedCustomer = new Customer(1L, "msa", "hin", "Oak Street 132","password", "john.doe@example.com", AccountStatus.USER);
//
//        when(customerRepository.findByEmail(anyString())).thenReturn(expectedCustomer);
//
//        Customer actualCustomer = customerService.findByEmail("john.doe@example.com");
//
//        assertEquals(expectedCustomer, actualCustomer);
//    }
//
//    /**
//     * Tests the getCurrentUser method of CustomerService.
//     * Verifies that the current customer returned matches the expected customer.
//     */
//    @Test
//    public void testGetCurrentCustomer() {
//        UserDetails userDetails = mock(UserDetails.class);
//        Authentication authentication = mock(Authentication.class);
//        SecurityContext securityContext = mock(SecurityContext.class);
//
//        when(userDetails.getUsername()).thenReturn("john.doe@example.com");
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        Customer expectedCustomer = new Customer(1L, "msa", "hin", "Oak Street 132","password", "john.doe@example.com", AccountStatus.USER);
//        when(customerRepository.findByEmail(anyString())).thenReturn(expectedCustomer);
//
//        Customer actualCustomer = customerService.getCurrentUser();
//
//        assertEquals(expectedCustomer, actualCustomer);
//    }
//
//    /**
//     * Tests the deleteCustomerById method of CustomerService.
//     * Verifies that the delete operation is invoked once for the specified customer ID.
//     */
//    @Test
//    public void testDeleteProduct() {
//        Long customerId = 1L;
//
//        doNothing().when(customerRepository).deleteById(customerId);
//
//        customerService.deleteCustomerById(customerId);
//
//        verify(customerRepository, times(1)).deleteById(customerId);
//    }
//
//    @Test
//    public void RegisterCustomer(){
//        PCrypt mockPCrypt = mock(PCrypt.class);
//        BCryptPasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);
//        when(mockPCrypt.passwordEncoder()).thenReturn(mockEncoder);
//        when(mockEncoder.encode("password123")).thenReturn("encryptedPassword123");
//        when(customerService.findByEmail("unique@example.com")).thenReturn(null);
//        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        CustomerService customerService = new CustomerService(customerRepository, mockPCrypt);
//        Customer newCustomer = new Customer(null, "John", "Doe", "123 Main St", "password123", "unique@example.com", AccountStatus.USER);
//
//        customerService.registerNewCustomer(newCustomer);
//
//        assertEquals("encryptedPassword123", newCustomer.getPassword());
//        verify(customerRepository).save(newCustomer);
//    }
//
//    @Test
//    public void test_update_existing_customer() {
//        CustomerRepository mockRepository = mock(CustomerRepository.class);
//        PCrypt mockCrypt = mock(PCrypt.class);
//        CustomerService customerService = new CustomerService(mockRepository, mockCrypt);
//        Long customerId = 1L;
//        Customer existingCustomer = new Customer(1L, "John", "Doe", "123 Main St", "password123", "john.doe@example.com", AccountStatus.USER);
//        Customer updatedCustomer = new Customer(1L, "Jane", "Doe", "123 Main St", "newpassword123", "jane.doe@example.com", AccountStatus.USER);
//
//        when(mockRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
//        doAnswer(invocation -> {
//            Customer savedCustomer = invocation.getArgument(0);
//            assertEquals("Jane", savedCustomer.getName());
//            assertEquals("Doe", savedCustomer.getSurname());
//            assertEquals("jane.doe@example.com", savedCustomer.getEmail());
//            assertEquals("newpassword123", savedCustomer.getPassword());
//            return null;
//        }).when(mockRepository).save(any(Customer.class));
//
//        Customer result = customerService.updateCustomer(customerId, updatedCustomer);
//
//        assertNotNull(result);
//        assertEquals("Jane", result.getName());
//        assertEquals("Doe", result.getSurname());
//        assertEquals("jane.doe@example.com", result.getEmail());
//        assertEquals("newpassword123", result.getPassword());
//    }
//
//
//    @Test
//    public void test_valid_token_and_admin_user() {
//        // Arrange
//        CustomerRepository mockCustomerRepository = mock(CustomerRepository.class);
//        PCrypt mockCrypt = mock(PCrypt.class);
//        CustomerService customerService = new CustomerService(mockCustomerRepository, mockCrypt);
//        Customer existingCustomer = new Customer(1L, "John", "Doe", "1234 Main St", "password123", "john.doe@example.com", AccountStatus.ADMIN);
//        Customer updatedCustomer = new Customer(1L, "John", "Doe", "1234 Main St", "newpassword123", "john.doe@example.com", AccountStatus.ADMIN);
//
//        Key key = Keys.hmacShaKeyFor(customerService.SECRETKEYSTRING.getBytes());
//        String token = Jwts.builder()
//                .subject("John")
//                .subject("93f725a07423fe1c889f448b33d21f46AFDASGHJKJFASDHGFASJASHDFJKAGSFGJAF93f725a07423fe1c889f448b33d21f46AFDASGHJKJFASDHGFASJASHDFJKAGSFGJAF....")
//                .issuedAt(new Date())
//                .signWith(key)
//                .compact();
//
//        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
//        when(mockCustomerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
//
//        // Act
//        Customer result = customerService.updateCustomerWithAuthorization(1L, updatedCustomer, token);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("newpassword123", result.getPassword());
//    }
//
//}