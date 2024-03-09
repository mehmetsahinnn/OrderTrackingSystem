package com.github.mehmetsahinnn.onlineordertrackingsystem.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findByName(String name);

    Customer findByEmail(String email);
}
