package com.example.spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stripeusers")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripe_id() {
        return stripe_id;
    }

    public void setStripe_id(String stripe_id) {
        this.stripe_id = stripe_id;
    }
}
