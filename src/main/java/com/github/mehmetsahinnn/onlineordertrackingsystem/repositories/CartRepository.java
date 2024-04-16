package com.github.mehmetsahinnn.onlineordertrackingsystem.repositories;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Cart;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartByCustomer(Customer customer);
}