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
    @Column(name = "stripe_id")
    private String stripe_id;

    public StripeUser() {}

    public StripeUser(String stripe_id ) {
        super();
        this.stripe_id = stripe_id;
    }
}
