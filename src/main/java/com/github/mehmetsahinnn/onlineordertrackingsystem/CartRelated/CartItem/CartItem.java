package com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.CartItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.mehmetsahinnn.onlineordertrackingsystem.CartRelated.cart.Cart;
import com.github.mehmetsahinnn.onlineordertrackingsystem.product.Product;
import lombok.*;

import jakarta.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cartitem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    private int quantity;

}