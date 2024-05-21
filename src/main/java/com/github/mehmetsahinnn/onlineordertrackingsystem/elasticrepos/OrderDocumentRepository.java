package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticrepos;

import com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments.OrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderDocumentRepository extends ElasticsearchRepository<OrderDocument, String> {
    void save(OrderDocument orderDocument);
}
