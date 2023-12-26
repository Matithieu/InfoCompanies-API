package com.example.spring.controller.Stripe;

import com.example.spring.controller.UserController;
import com.example.spring.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;

public class CustomerUtil {

    public static Customer retrieveCustomer(String customerId) throws StripeException {
        return Customer.retrieve(customerId);
    }

    public static Customer findCustomerByEmail(String email) throws StripeException {
        CustomerSearchParams params =
                CustomerSearchParams
                        .builder()
                        .setQuery("email:'" + email + "'")
                        .build();

        CustomerSearchResult result = Customer.search(params);

        return !result.getData().isEmpty() ? result.getData().get(0) : null;
    }

    public static Customer findOrCreateCustomer(User user) throws StripeException {
        Customer result = retrieveCustomer(user.getStripe_api().getStripe_id());
        Customer customer;

        // If no existing customer was found, create a new record
        if (result.getObject().isEmpty()) {
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();

            customer = Customer.create(customerCreateParams);
        } else {
            customer = result;
        }

        return customer;
    }
}