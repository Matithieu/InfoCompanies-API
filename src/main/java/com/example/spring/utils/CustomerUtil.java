package com.example.spring.utils;

import com.example.spring.DTO.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;

import java.util.concurrent.Semaphore;

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
                        .setName(user.getFirstName() + " " + user.getLastName())
                        .setEmail(user.getEmail())
                        .setPhone(user.getPhone())
                        .setAddress(
                                CustomerCreateParams.Address.builder()
                                        .setCity(user.getLocality())
                                        .setLine1(user.getStreet())
                                        .setPostalCode(user.getPostalCode())
                                        .build())
                        .setDescription("Customer for " + user.getEmail())
                        .putMetadata("user_id", user.getId())
                        .build();

                RequestOptions requestOptions = RequestOptions.builder()
                        .setIdempotencyKey(user.getId())
                        .build();

                // Sometimes, to debug remove the idempotency key
                customer = Customer.create(customerCreateParams, requestOptions);
                System.out.println("Customer created: " + customer.getId());
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