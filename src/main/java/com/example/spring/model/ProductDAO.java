package com.example.spring.model;

import com.stripe.model.Price;
import com.stripe.model.Product;

import java.math.BigDecimal;

public class ProductDAO {
    static Product product;

    static {
        product = new Product();

        Product sampleProduct = new Product();
        Price samplePrice = new Price();

        sampleProduct.setName("Puma Shoes");
        sampleProduct.setDescription("Puma Shoes");
        sampleProduct.setId("shoe");
        samplePrice.setCurrency("eur");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(3 * 100 * 10)); // 30 euros
        sampleProduct.setDefaultPriceObject(samplePrice);
        product = sampleProduct;
    }

    public static Product getProduct() {
        return product;
    }
}