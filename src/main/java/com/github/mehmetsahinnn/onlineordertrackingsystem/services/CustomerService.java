package com.github.mehmetsahinnn.onlineordertrackingsystem.services;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import com.github.mehmetsahinnn.onlineordertrackingsystem.repositories.CustomerRepository;
import com.github.mehmetsahinnn.onlineordertrackingsystem.security.PCrypt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * The CustomerService class provides methods for managing customers.
 * It includes methods for finding a customer by name and saving a customer.
 */
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PCrypt crypt;
    final String SECRETKEYSTRING = "93f725a07423fe1c889f448b33d21f46AFDASGHJKJFASDHGFASJASHDFJKAGSFGJAF93f725a07423fe1c889f448b33d21f46AFDASGHJKJFASDHGFASJASHDFJKAGSFGJAF....";

    /**
     * Constructs a new CustomerService with the specified CustomerRepository.
     *
     * @param customerRepository the CustomerRepository to be used by the CustomerService
     * @param crypt              the crypt class to be used by CustomerService
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

    /**
     * Builds a JWT token for the given customer.
     *
     * @param customer the customer for whom the token is to be built
     * @return the JWT token as a string
     */
    public String tokenBuilder(Customer customer) {
        int tokenExpiration = 86400000; // 1 gün
        Key secretKey = Keys.hmacShaKeyFor(SECRETKEYSTRING.getBytes());
        return Jwts.builder()
                .subject(customer.getName())
                .claim("customerId", customer.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Updates the details of a customer with the given ID.
     *
     * @param id the ID of the customer to update
     * @param updatedCustomer the new details of the customer
     * @return the updated customer, or null if no customer was found with the given ID
     * @throws RuntimeException if an error occurred while updating the customer
     */
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        try {
            Customer existingCustomer = customerRepository.findById(id).orElse(null);
            if (existingCustomer != null) {
                existingCustomer.setName(updatedCustomer.getName());
                existingCustomer.setSurname(updatedCustomer.getSurname());
                existingCustomer.setEmail(updatedCustomer.getEmail());
                existingCustomer.setPassword(updatedCustomer.getPassword());
                customerRepository.save(existingCustomer);
            }
            return existingCustomer;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the customer", e);
        }
    }

    /**
     * Updates the details of a customer with the given ID, if the token is authorized to do so.
     *
     * @param id the ID of the customer to update
     * @param updatedCustomer the new details of the customer
     * @param token the JWT token used for authorization
     * @return the updated customer, or null if no customer was found with the given ID
     * @throws RuntimeException if the token is not authorized to update the customer, or if an error occurred while updating the customer
     */
    public Customer updateCustomerWithAuthorization(Long id, Customer updatedCustomer, String token) {
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRETKEYSTRING));
        Claims claims = Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
        Long customerId = Long.parseLong(claims.get("customerId").toString());
        boolean isAdmin = Boolean.parseBoolean(claims.get("isAdmin").toString());

        if (isAdmin || customerId.equals(id)) {
            return updateCustomer(id, updatedCustomer);
        } else {
            throw new RuntimeException("You are not authorized to update this customer");
        }
    }
}