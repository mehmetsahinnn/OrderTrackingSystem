package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDocumentRepository extends ElasticsearchRepository<OrderDocument, String>{
    OrderDocument save(OrderDocument orderDocument);

    @Query("{\"match\": {\"status\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\", \"prefix_length\": 3}}}")
    List<OrderDocument> searchByStatusWithFuzziness(String status);

    @Query("{\"bool\": {\"must\": [{\"range\": {\"orderDate\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}]}}")
    List<OrderDocument> findOrdersByDateBetween(LocalDateTime startDate, LocalDateTime endDate);


    List<OrderDocument> findUserByUsername(String username);


//    @Query("{\"match_all\": {}}")
//    List<OrderDocument> findAll();
}
