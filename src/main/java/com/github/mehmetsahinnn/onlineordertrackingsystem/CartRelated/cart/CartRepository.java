package com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart;

import com.github.mehmetsahinnn.onlineordertrackingsystem.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartByCustomer(Customer customer);
}