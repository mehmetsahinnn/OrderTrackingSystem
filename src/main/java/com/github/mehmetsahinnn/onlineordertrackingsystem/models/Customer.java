package com.github.mehmetsahinnn.onlineordertrackingsystem.models;

import com.github.mehmetsahinnn.onlineordertrackingsystem.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")

public class Customer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "isadmin")
    private AccountStatus status;

}