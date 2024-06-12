package com.github.mehmetsahinnn.onlineordertrackingsystem.elasticdocuments;

import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "order", createIndex = true)
public class OrderDocument {


    @Id
    @Field(name="id", type = FieldType.Keyword)
    private String id;

    private String username;

    private Long customerId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "okay", type = FieldType.Keyword)
            }
    )
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime estimatedDeliveryDate;

}