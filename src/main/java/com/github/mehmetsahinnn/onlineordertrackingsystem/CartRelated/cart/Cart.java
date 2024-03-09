package com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart;

import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem.CartItem;
import com.github.mehmetsahinnn.onlineordertrackingsystem.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;
}