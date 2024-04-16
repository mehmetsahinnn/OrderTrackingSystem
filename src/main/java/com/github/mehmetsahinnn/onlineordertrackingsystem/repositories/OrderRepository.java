package com.github.mehmetsahinnn.onlineordertrackingsystem.repositories;


import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
