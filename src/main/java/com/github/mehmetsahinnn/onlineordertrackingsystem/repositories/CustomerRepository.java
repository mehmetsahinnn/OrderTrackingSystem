package com.github.mehmetsahinnn.onlineordertrackingsystem.repositories;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findByName(String name);

    Customer findByEmail(String email);
}
