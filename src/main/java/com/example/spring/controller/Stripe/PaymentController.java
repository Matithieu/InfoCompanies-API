package com.example.spring.controller.Stripe;

import com.example.spring.controller.UserController;
import com.example.spring.model.ProductDAO;
import com.example.spring.model.RequestDTO;
import com.example.spring.model.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

// https://kinsta.com/blog/stripe-java-api/

@RestController
@CrossOrigin
public class PaymentController {

    @Autowired
    private UserController userController;

    private Semaphore mutex = new Semaphore(1);

    String STRIPE_API_KEY = "sk_test_51OMa7QKjCboMtBPji8gnW9Us60F7iDnMTh50lQoRXsMN5Fm19kF3sXOxB5uXNPEe250MAmnfgLLc5oOYtlRNfYpe00ukE5BD1d";
    //String STRIPE_API_KEY = System.getenv().get("STRIPE_API_KEY");


    @PostMapping("/create-payment-intent/")
    String hostedCheckout(@RequestBody RequestDTO requestDTO) throws Exception {
        try {
            mutex.acquire();
            Stripe.apiKey = STRIPE_API_KEY;

            System.out.println(requestDTO.getCustomerEmail());
            // Find the user record from the database
            User user = userController.getUserByEmail(requestDTO.getCustomerEmail());

            System.out.println(user);

            if (user != null && user.getVerified() != false) {
                // Start by finding an existing customer record from Stripe or creating a new one if needed
                Customer customer = CustomerUtil.findOrCreateCustomer(user);
                System.out.println(customer);

                RequestOptions requestOptions = RequestOptions.builder()
                        .setIdempotencyKey(user.getId().toString())
                        .build();

                // Create a PaymentIntent with the order amount and currency
                PaymentIntentCreateParams params =
                        PaymentIntentCreateParams.builder()
                                .setAmount((long) ProductDAO.getProduct().getDefaultPriceObject().getUnitAmountDecimal().floatValue())
                                .setDescription(ProductDAO.getProduct().getDescription())
                                .setCurrency("eur")
                                .setCustomer(customer.getId())
                                .setAutomaticPaymentMethods(
                                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                                .builder()
                                                .setEnabled(true)
                                                .build()
                                )

                                .build();

                PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);

                // Send publishable key and PaymentIntent details to client

                return paymentIntent.getClientSecret();
            } else {
                return "User not found";
            }
        } catch (InterruptedException e) {
            throw new Exception("Error when fetching /create-payment-intent/ ", e);
        } finally {
            mutex.release();
        }
    }

    @PostMapping("/subscriptions/trial")
    String newSubscriptionWithTrial(@RequestBody RequestDTO requestDTO) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;

        String clientBaseURL = System.getenv().get("CLIENT_BASE_URL");

        // Find the user record from the database
        User user = userController.getUserByEmail(requestDTO.getCustomerEmail());

        if (user != null) {
            // Start by finding existing customer record from Stripe or creating a new one if needed
            Customer customer = CustomerUtil.findOrCreateCustomer(user);

            // Next, create a checkout session by adding the details of the checkout
            SessionCreateParams.Builder paramsBuilder =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                            .setCustomer(customer.getId())
                            .setSuccessUrl(clientBaseURL + "/success?session_id={CHECKOUT_SESSION_ID}")
                            .setCancelUrl(clientBaseURL + "/failure")
                            // For trials, you need to set the trial period in the session creation request
                            .setSubscriptionData(SessionCreateParams.SubscriptionData.builder().setTrialPeriodDays(30L).build());


            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    PriceData.builder()
                                            .setProductData(
                                                    PriceData.ProductData.builder()
                                                            .putMetadata("app_id", requestDTO.getItem().getId())
                                                            .setName(requestDTO.getItem().getName())
                                                            .build()
                                            )
                                            .setCurrency(ProductDAO.getProduct().getDefaultPriceObject().getCurrency())
                                            .setUnitAmountDecimal(ProductDAO.getProduct().getDefaultPriceObject().getUnitAmountDecimal())
                                            .setRecurring(PriceData.Recurring.builder().setInterval(PriceData.Recurring.Interval.MONTH).build())
                                            .build())
                            .build());

            Session session = Session.create(paramsBuilder.build());

            return session.getUrl();

        } else {
            return "User not found";
        }
    }

    @PostMapping("/invoices/list")
    List<Map<String, String>> listInvoices(@RequestBody RequestDTO requestDTO) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;

        // Start by finding existing customer record from Stripe
        Customer customer = CustomerUtil.findCustomerByEmail(requestDTO.getCustomerEmail());

        // If no customer record was found, no subscriptions exist either, so return an empty list
        if (customer == null) {
            return new ArrayList<>();
        }

        // Search for invoices for the current customer
        Map<String, Object> invoiceSearchParams = new HashMap<>();
        invoiceSearchParams.put("customer", customer.getId());
        InvoiceCollection invoices =
                Invoice.list(invoiceSearchParams);

        List<Map<String, String>> response = new ArrayList<>();

        // For each invoice, extract its number, amount, and PDF URL to send to the client
        for (Invoice invoice : invoices.getData()) {
            HashMap<String, String> map = new HashMap<>();

            map.put("number", invoice.getNumber());
            map.put("amount", String.valueOf((invoice.getTotal() / 100f)));
            map.put("url", invoice.getInvoicePdf());

            response.add(map);
        }

        return response;
    }
}