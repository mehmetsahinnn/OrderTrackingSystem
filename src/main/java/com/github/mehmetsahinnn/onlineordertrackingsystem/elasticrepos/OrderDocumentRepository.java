package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDocumentRepository extends ElasticsearchRepository<OrderDocument, String> {
    OrderDocument save(OrderDocument orderDocument);

    List<OrderDocument> findByStatus(String status);
}
