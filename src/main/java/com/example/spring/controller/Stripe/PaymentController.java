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
import com.stripe.param.SubscriptionItemListParams;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

// https://kinsta.com/blog/stripe-java-api/

@RestController
@CrossOrigin
public class PaymentController {

    @Autowired
    private UserController userController;

    private static final Semaphore mutex = new Semaphore(1);

    String STRIPE_API_KEY = "sk_test_51OMa7QKjCboMtBPji8gnW9Us60F7iDnMTh50lQoRXsMN5Fm19kF3sXOxB5uXNPEe250MAmnfgLLc5oOYtlRNfYpe00ukE5BD1d";
    //String STRIPE_API_KEY = System.getenv().get("STRIPE_API_KEY");

    @PostMapping("/subscriptions/trial")
    String newSubscriptionWithTrial(@RequestBody RequestDTO requestDTO) throws Exception {
        Stripe.apiKey = STRIPE_API_KEY;

        //String clientBaseURL = System.getenv().get("CLIENT_BASE_URL");
        String clientBaseURL = "http://localhost:5173";

        // Find the user record from the database
        User user = userController.getUserByEmail(requestDTO.getCustomerEmail());
        
        try {
            mutex.acquire();
            if (user != null && !user.getVerified()) {
                // Start by finding existing customer record from Stripe or creating a new one if needed
                Customer customer = CustomerUtil.findOrCreateCustomer(user);

                System.out.println("User trying to subscribe: " + user.getEmail());

                // Next, create a checkout session by adding the details of the checkout
                SessionCreateParams.Builder paramsBuilder =
                        SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                                .setCustomer(customer.getId())
                                .setSuccessUrl(clientBaseURL + "/completion?session_id={CHECKOUT_SESSION_ID}")
                                .setCancelUrl(clientBaseURL + "/failure")
                                .setClientReferenceId(user.getId().toString())
                                // For trials, you need to set the trial period in the session creation request
                                .setSubscriptionData(SessionCreateParams.SubscriptionData.builder().setTrialPeriodDays(3L).build());

                // Add the all the details to the session creation request
                paramsBuilder.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        PriceData.builder()
                                                .setProductData(
                                                        PriceData.ProductData.builder()
                                                                .putMetadata("app_id", ProductDAO.getProduct(requestDTO.getItem()).getId())
                                                                .setName(ProductDAO.getProduct(requestDTO.getItem()).getName())
                                                                .setDescription(ProductDAO.getProduct(requestDTO.getItem()).getDescription())
                                                                .build()
                                                )
                                                .setCurrency(ProductDAO.getProduct(requestDTO.getItem()).getDefaultPriceObject().getCurrency())
                                                .setUnitAmountDecimal(ProductDAO.getProduct(requestDTO.getItem()).getDefaultPriceObject().getUnitAmountDecimal())
                                                .setRecurring(PriceData.Recurring.builder().setInterval(PriceData.Recurring.Interval.MONTH).build())
                                                .build())
                                .build());

                RequestOptions requestOptions = RequestOptions.builder()
                        .setIdempotencyKey(user.getId().toString())
                        .build();

                Session session = Session.create(paramsBuilder.build());

                return session.getUrl();

            } else if (user != null && user.getVerified()) {
                return "User is already verified";
            }
            else {
                return "User not found";
            }
        } 
        catch (StripeException e) {
            throw new Exception("Error when fetching /subscriptions/trial ", e);
        } finally {
            mutex.release();
        }
    }

    @PostMapping("/invoices/list")
    List<Map<String, String>> listInvoices(@RequestBody RequestDTO requestDTO) throws Exception {

        Stripe.apiKey = STRIPE_API_KEY;

        // Verify that the user exists
        User user = userController.getUserByEmail(requestDTO.getCustomerEmail());
        Customer customer = CustomerUtil.findCustomerByEmail(user.getEmail());

        try {
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
        catch (StripeException e) {
            throw new Exception("Error when fetching /invoices/list ", e);
        }
    }

    @PostMapping("/subscriptions/list")
    List<Map<String, String>> viewSubscriptions(@RequestBody RequestDTO requestDTO) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;

        // Start by finding existing customer record from Stripe
        Customer customer = CustomerUtil.findCustomerByEmail(requestDTO.getCustomerEmail());

        // If no customer record was found, no subscriptions exist either, so return an empty list
        if (customer == null) {
            return new ArrayList<>();
        }

        // Search for subscriptions for the current customer
        SubscriptionCollection subscriptions = Subscription.list(
                SubscriptionListParams.builder()
                        .setCustomer(customer.getId())
                        .build());

        List<Map<String, String>> response = new ArrayList<>();

        // For each subscription record, query its item records and collect in a list of objects to send to the client
        for (Subscription subscription : subscriptions.getData()) {
            SubscriptionItemCollection currSubscriptionItems =
                    SubscriptionItem.list(SubscriptionItemListParams.builder()
                            .setSubscription(subscription.getId())
                            .addExpand("data.price.product")
                            .build());

            for (SubscriptionItem item : currSubscriptionItems.getData()) {
                HashMap<String, String> subscriptionData = new HashMap<>();
                subscriptionData.put("appProductId", item.getPrice().getProductObject().getMetadata().get("app_id"));
                subscriptionData.put("subscriptionId", subscription.getId());
                subscriptionData.put("subscribedOn", new SimpleDateFormat("dd/MM/yyyy").format(new Date(subscription.getStartDate() * 1000)));
                subscriptionData.put("nextPaymentDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date(subscription.getCurrentPeriodEnd() * 1000)));
                subscriptionData.put("price", item.getPrice().getUnitAmountDecimal().toString());

                if (subscription.getTrialEnd() != null && new Date(subscription.getTrialEnd() * 1000).after(new Date()))
                    subscriptionData.put("trialEndsOn", new SimpleDateFormat("dd/MM/yyyy").format(new Date(subscription.getTrialEnd() * 1000)));
                response.add(subscriptionData);
            }

        }

        return response;
    }

    @PostMapping("/subscriptions/cancel")
    String cancelSubscription(@RequestBody RequestDTO requestDTO) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        Subscription subscription =
                Subscription.retrieve(
                        requestDTO.getSubscriptionId()
                );

        Subscription deletedSubscription =
                subscription.cancel();

        return deletedSubscription.getStatus();
    }
}