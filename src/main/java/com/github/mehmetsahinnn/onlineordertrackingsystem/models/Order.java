package com.github.mehmetsahinnn.onlineordertrackingsystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ordertrackid", columnDefinition = "uuid")
    private UUID orderTrackId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerid")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    @JsonBackReference
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "orderdate")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date orderDate;

    @Column(name = "estimateddeliverydate")
    private LocalDate estimatedDeliveryDate;

    @Override
    public String toString() {
        return "Order{" +
                "id = '" + id + '\'' +
                ", customer = '" + customer +
                ", orderItems = " + orderItems +
                ", status = " + status +
                ", orderDate = " + orderDate + '\'' +
                ", estimatedDeliveryDate = '" + estimatedDeliveryDate + '\'' +
                '}';
    }

}