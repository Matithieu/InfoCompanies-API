package com.example.spring.model;

import com.stripe.model.Product;

public class RequestDTO {
    Product item;
    String customerName;
    String customerEmail;

    public Product getItem() {
        return item;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}