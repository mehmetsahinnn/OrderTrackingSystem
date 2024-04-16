package com.github.mehmetsahinnn.onlineordertrackingsystem.config;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class CustomerResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Customer customer, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status.value());
        map.put("message", message);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("customerId", customer.getId());
        data.put("name", customer.getName());
        data.put("surname", customer.getSurname());
        data.put("email", customer.getEmail());
        data.put("isAdmin", customer.getIsAdmin());
        map.put("data", data);

        return new ResponseEntity<>(map, status);
    }
}