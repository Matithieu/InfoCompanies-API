package com.example.spring.model;

import com.stripe.model.Price;
import com.stripe.model.Product;

import java.math.BigDecimal;

public class ProductDAO {
    static Product[] products;

    static {
        products = new Product[4];

        Product sampleProduct = new Product();
        Price samplePrice = new Price();

        sampleProduct.setName("Basic subscription");
        sampleProduct.setId("basic");
        sampleProduct.setDescription("Basic subscription");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(3 * 100 *10));
        samplePrice.setCurrency("eur");
        sampleProduct.setDefaultPriceObject(samplePrice);
        products[0] = sampleProduct;

        sampleProduct = new Product();
        samplePrice = new Price();

        sampleProduct.setName("Premium subscription");
        sampleProduct.setId("premium");
        sampleProduct.setDescription("Premium subscription");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(5 * 100 *10));
        samplePrice.setCurrency("eur");
        sampleProduct.setDefaultPriceObject(samplePrice);
        products[1] = sampleProduct;

        sampleProduct = new Product();
        samplePrice = new Price();

        sampleProduct.setName("Enterprise subscription");
        sampleProduct.setId("enterprise");
        sampleProduct.setDescription("Enterprise subscription");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(10 * 100 *10));
        samplePrice.setCurrency("eur");
        sampleProduct.setDefaultPriceObject(samplePrice);
        products[2] = sampleProduct;
    }

    public static Product getProduct(String id) {
        if ("basic".equals(id)) {
            return products[0];
        } else if ("premium".equals(id)) {
            return products[1];
        } else if ("enterprise".equals(id)) {
            return products[2];
        } else return new Product();

    }
}