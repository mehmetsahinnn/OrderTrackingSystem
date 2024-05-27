package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments;

import lombok.*;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "order", createIndex = true)
public class OrderDocument {


    @Id
    @Field(name="id", type = FieldType.Keyword)
    private String id;


    private Long customerId;

    private String status;
    private Date orderDate;
    private Long estimatedDeliveryDate;

}