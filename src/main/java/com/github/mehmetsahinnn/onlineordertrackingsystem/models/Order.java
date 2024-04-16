package com.github.mehmetsahinnn.onlineordertrackingsystem.models;

import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "orderDate")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date orderDate;

    @Column(name = "estimatedDeliveryDate")
    private LocalDate estimatedDeliveryDate;

}