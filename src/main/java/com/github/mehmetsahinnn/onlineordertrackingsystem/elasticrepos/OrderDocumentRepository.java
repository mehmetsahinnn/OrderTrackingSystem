package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import com.github.mehmetsahinnn.onlineordertrackingsystem.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDocumentRepository extends ElasticsearchRepository<OrderDocument, String>{
    OrderDocument save(OrderDocument orderDocument);

    @Query("{\"match\": {\"status\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\", \"prefix_length\": 3}}}")
    List<OrderDocument> searchByStatusWithFuzziness(String status);


    @Query("{\"range\": {\"orderDate\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}")
    List<OrderDocument> findOrdersByDateBetween(LocalDate startDate, LocalDate endDate);


//    @Query("{\"match_all\": {}}")
//    List<OrderDocument> findAll();
}
