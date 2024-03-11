package com.github.mehmetsahinnn.onlineordertrackingsystem.customer;

import com.github.mehmetsahinnn.onlineordertrackingsystem.security.PCrypt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The CustomerService class provides methods for managing customers.
 * It includes methods for finding a customer by name and saving a customer.
 */
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PCrypt crypt;

    /**
     * Constructs a new CustomerService with the specified CustomerRepository.
     *
     * @param customerRepository the CustomerRepository to be used by the CustomerService
     * @param crypt the crypt class to be used by CustomerService
     */
    public CustomerService(CustomerRepository customerRepository, PCrypt crypt) {
        this.customerRepository = customerRepository;
        this.crypt = crypt;
    }

    /**
     * Saves a customer.
     *
     * @param customer the customer to save
     * @return the saved customer
     */
    public Customer saveCustomer(Customer customer) {
        try {
            return customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the customer", e);
        }
    }

    /**
     * Finds a customer by email.
     *
     * @param email the email of the customer to find
     * @return the found customer, or null if no customer was found
     */
    public Customer findByEmail(String email) {
        try {
            return customerRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while finding the customer", e);
        }
    }

    /**
     * Returns the currently authenticated user.
     *
     * @return the currently authenticated user
     */
    public Customer getCurrentUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                return findByEmail(email);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all customers from the repository.
     *
     * @return A list of all customers
     * @throws RuntimeException If an error occurs while retrieving the customers
     */
    public List<Customer> findAll() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Deletes a customer with the specified ID from the repository.
     *
     * @param id The ID of the customer to be deleted
     * @throws RuntimeException If an error occurs while deleting the customer
     */
    public void deleteCustomerById(Long id) {
        try {
            customerRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers a new customer in the system.
     *
     * @param customer the customer object to be registered
     * @throws RuntimeException if a user with the same email already exists in the system
     */
    public void registerNewCustomer(Customer customer) {
        if (findByEmail(customer.getEmail()) != null) {
            throw new RuntimeException("User already exists with email: " + customer.getEmail());
        }

        String encryptedPassword = crypt.passwordEncoder().encode(customer.getPassword());
        customer.setPassword(encryptedPassword);

        saveCustomer(customer);
    }

}