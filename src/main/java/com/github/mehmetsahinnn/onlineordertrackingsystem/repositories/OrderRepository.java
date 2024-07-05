package com.github.mehmetsahinnn.onlineordertrackingsystem.repositories;


import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByOrderTrackId(UUID orderTrackId);
}
