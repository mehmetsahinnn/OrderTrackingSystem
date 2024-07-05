package com.github.mehmetsahinnn.onlineordertrackingsystem.repositories;

import com.github.mehmetsahinnn.onlineordertrackingsystem.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
