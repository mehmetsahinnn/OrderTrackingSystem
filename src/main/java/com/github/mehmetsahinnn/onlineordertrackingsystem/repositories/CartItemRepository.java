package com.github.mehmetsahinnn.onlineordertrackingsystem.repositories;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Customer;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByProductAndCustomer(Product product, Customer customer);

}
