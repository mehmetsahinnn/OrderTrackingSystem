package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;
//
//import com.github.mehmetsahinnn.onlineordertrackingsystem.annotations.Authentication;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.KeycloakClient;
import com.github.mehmetsahinnn.onlineordertrackingsystem.services.CustomerService;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.CustomerResponseHandler;
import com.github.mehmetsahinnn.onlineordertrackingsystem.config.ResponseHandler;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import com.github.mehmetsahinnn.onlineordertrackingsystem.security.PCrypt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * The CustomerController class provides RESTful API endpoints for managing customer-related actions.
 * It includes endpoints for login and registration.
 */
@RestController
@RequestMapping("/api")
@Log4j2
public class CustomerController extends BaseController{

    private final CustomerService customerService;
    private final PCrypt crypt;
    private final KeycloakClient keycloakClient;


    /**
     * Constructs a new CustomerController with the specified CustomerService and PCrypt.
     *
     * @param customerService the CustomerService to be used by the CustomerController
     * @param crypt           the PCrypt to be used by the CustomerController
     */
    @Autowired
    public CustomerController(CustomerService customerService, PCrypt crypt,  KeycloakClient keycloakClient) {
        this.customerService = customerService;
        this.crypt = crypt;
        this.keycloakClient = keycloakClient;
    }

    /**
     * Logs in a customer and returns a JWT token.
     *
     * @param customerLoginDetails the login details of the customer
     * @return a ResponseEntity containing the JWT token if login is successful, or an error message otherwise
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customerLoginDetails) {
        return handleRequest(() -> {
            Customer customer = customerService.findByEmail(customerLoginDetails.getEmail());
            if (customer != null && crypt.passwordEncoder().matches(customerLoginDetails.getPassword(), customer.getPassword())) {
                String token = keycloakClient.getLoginToken(customer.getEmail(), customer.getPassword());
                return CustomerResponseHandler.generateResponse("Logged In", HttpStatus.OK, customer, token);
            } else {
                return ResponseHandler.generateResponse("Invalid email or password", HttpStatus.UNAUTHORIZED, null);
            }
        }, "login");
    }

    /**
     * Registers a new customer.
     *
     * @param customer the details of the customer to register
     * @return a ResponseEntity containing the registered customer details with encrypted password
     * if registration is successful, or an error message otherwise
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Customer customer) {
        return handleRequest(() -> {
            customerService.registerNewCustomer(customer);
            keycloakClient.createUser(customer.getEmail(), customer.getPassword(), customer.getName(), customer.getSurname());
            String token = keycloakClient.getLoginToken(customer.getEmail(), customer.getPassword());
            return ResponseHandler.generateResponse("Registration successful", HttpStatus.OK, token);
        }, "register");
    }
    /**
     * Retrieves a list of all customers.
     *
     * @return a ResponseEntity containing the list of customers if retrieval is successful, or an error message otherwise
     */
    @GetMapping("/customers")
    public ResponseEntity<?> listCustomers() {
        return handleRequest(customerService::findAll, "customers");
    }

    /**
     * Updates a customer's details.
     *
     * @param id              the ID of the customer to update
     * @param updatedCustomer the new details of the customer
     * @param request         the HttpServletRequest containing the JWT token
     * @return a ResponseEntity containing the updated customer details if update is successful, or an error message otherwise
     */
    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer, HttpServletRequest request) {
        return handleRequest(() -> {
            String token = request.getHeader("Authorization").substring(7);
            return customerService.updateCustomerWithAuthorization(id, updatedCustomer, token);
        }, "updateCustomer");
    }

    /**
     * Deletes a customer.
     *
     * @param id the ID of the customer to delete
     * @return a ResponseEntity containing the HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return handleRequest(() -> {
            customerService.deleteCustomerById(id);
            return null;
        }, "deleteCustomer");
    }
}