package com.example.spring.controller.DTO;

import com.stripe.model.Product;

public class RequestDTO {
    String item;
    String customerName;
    String customerEmail;

    String subscriptionId;


    public String getItem() {
        return item;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }
}