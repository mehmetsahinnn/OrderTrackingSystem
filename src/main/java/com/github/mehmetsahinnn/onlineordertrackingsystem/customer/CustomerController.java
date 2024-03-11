package com.github.mehmetsahinnn.onlineordertrackingsystem.customer;

import com.github.mehmetsahinnn.onlineordertrackingsystem.security.PCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The CustomerController class provides RESTful API endpoints for managing customer-related actions.
 * It includes endpoints for login and registration.
 */
@Controller
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;
    private final PCrypt crypt;

    /**
     * Constructs a new CustomerController with the specified CustomerService and PCrypt.
     *
     * @param customerService the CustomerService to be used by the CustomerController
     * @param crypt the PCrypt to be used by the CustomerController
     */
    @Autowired
    public CustomerController(CustomerService customerService, PCrypt crypt) {
        this.customerService = customerService;
        this.crypt = crypt;
    }

    /**
     * Logs in a customer.
     *
     * @param customerLoginDetails the login details of the customer
     * @return a ResponseEntity containing the customer details if login is successful, or an error message otherwise
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customerLoginDetails){
        try {
            Customer customer = customerService.findByEmail(customerLoginDetails.getEmail());
            if (customer != null && crypt.passwordEncoder().matches(customerLoginDetails.getPassword(), customer.getPassword())){
                return new ResponseEntity<>(customer, HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while logging in", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            if (customerService.findByEmail(customer.getEmail()) != null) {
                return new ResponseEntity<>("User Already Exists ! ", HttpStatus.CONFLICT);
            }
            else {
                String encryptedPassword = crypt.passwordEncoder().encode(customer.getPassword());
                customer.setPassword(encryptedPassword);

                Customer savedUser = customerService.saveCustomer(customer);
                return ResponseEntity.ok(savedUser);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while registering", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customers")
    public ResponseEntity<?> listCustomers() {
        try {
            List<Customer> customers = customerService.findAll();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while retrieving customers", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}