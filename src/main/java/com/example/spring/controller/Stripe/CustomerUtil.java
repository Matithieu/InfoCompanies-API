package com.example.spring.controller.Stripe;

import com.example.spring.controller.UserController;
import com.example.spring.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import static javax.swing.UIManager.put;

public class CustomerUtil {
    private static final Semaphore mutex = new Semaphore(1);

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

    public static Customer findOrCreateCustomer(User user) throws Exception {
        mutex.acquire();
        try {
            CustomerSearchParams params =
                    CustomerSearchParams
                            .builder()
                            .setQuery("email:'" + user.getEmail() + "'")
                            .build();

            CustomerSearchResult result = Customer.search(params);

            Customer customer;

            // If no existing customer was found, create a new record
            if (result.getData().isEmpty()) {

                CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                        .setName(user.getName())
                        .setEmail(user.getEmail())
                        .setPhone(user.getPhone())
                        .setAddress(
                                CustomerCreateParams.Address.builder()
                                        .setCity(user.getCity())
                                        .setLine1(user.getAddress())
                                        .setPostalCode("13100")
                                        .build())
                        .setDescription("Customer for " + user.getEmail())
                        .build();

                RequestOptions requestOptions = RequestOptions.builder()
                        .setIdempotencyKey(user.getId().toString() + "testa")
                        .build();

                // Sometimes, to debug remove the idempotency key
                customer = Customer.create(customerCreateParams, requestOptions);
                System.out.println("New customer created: " + customer.getId());
            } else {
                customer = result.getData().get(0);
            }
            return customer;
        } catch (StripeException e) {
            throw new Exception("Error during the creation or the retrieve of the stripe account ", e);
        } finally {
            mutex.release();
        }
    }
}