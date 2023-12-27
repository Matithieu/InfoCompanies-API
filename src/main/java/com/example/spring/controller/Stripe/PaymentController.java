package com.example.spring.controller.Stripe;

import com.example.spring.controller.UserController;
import com.example.spring.model.ProductDAO;
import com.example.spring.model.RequestDTO;
import com.example.spring.model.User;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SubscriptionItemListParams;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                .setBillingAddressCollection(
                                        SessionCreateParams.BillingAddressCollection.REQUIRED
                                )
                                .setClientReferenceId(user.getId().toString());

                // For trials, you need to set the trial period in the session creation request
                //.setSubscriptionData(SessionCreateParams.SubscriptionData.builder().setTrialPeriodDays(0L).build());

                // Add the all the details to the session creation request
                paramsBuilder
                        .addLineItem(
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
                                        .build()
                        )
                        .setPhoneNumberCollection(
                                SessionCreateParams.PhoneNumberCollection.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .setCustomText(
                                SessionCreateParams.CustomText.builder()
                                        .setSubmit(
                                                SessionCreateParams.CustomText.Submit.builder()
                                                        .setMessage("You can refund freely during 14 days.")
                                                        .build()
                                        )
                                        .build()
                        )
                ;

                RequestOptions requestOptions = RequestOptions.builder()
                        .setIdempotencyKey(user.getId().toString())
                        .build();

                Session session = Session.create(paramsBuilder.build());

                return session.getUrl();

            } else if (user != null && user.getVerified()) {
                return "User is already verified";
            } else {
                return "User not found";
            }
        } catch (StripeException e) {
            throw new Exception("Error when fetching /subscriptions/trial ", e);
        } finally {
            mutex.release();
        }
    }

    @CrossOrigin
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        // stripe listen --forward-to localhost:8080/webhook
        String endpointSecret = "whsec_9552112da604d21f5b0bc4965a70031695a07006a6abcd374b60be44fd7faf99"; // Replace with your endpoint's secret
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            // Invalid signature
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // Handle the event
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();

            // Fulfill the purchase...
            System.out.println("Checkout session completed: " + session.getCustomerEmail());
            System.out.println("Session ID: " + session.getClientReferenceId());
            User user = userController.getUserByEmail(session.getCustomerEmail());
            user.setVerified(true);
        }

        return ResponseEntity.ok("Received");
    }
}