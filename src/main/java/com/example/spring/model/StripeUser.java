package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stripeusers")
@Setter
@Getter
public class StripeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "stripeId")
    private String stripeId;

    public StripeUser() {}

    public StripeUser(String stripeId ) {
        super();
        this.stripeId = stripeId;
    }
}
