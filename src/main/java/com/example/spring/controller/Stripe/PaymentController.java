package com.example.spring.controller.Stripe;

import com.example.spring.DAO.ProductDAO;
import com.example.spring.DTO.RequestDTO;
import com.example.spring.DTO.User;
import com.example.spring.keycloakclient.UserResource;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Semaphore;

// https://kinsta.com/blog/stripe-java-api/

@RestController
@CrossOrigin
public class PaymentController {

    @Autowired
    private UserResource userResource;

    private static final Semaphore mutex = new Semaphore(1);

    @Value("${STRIPE_API_KEY}")
    private String STRIPE_API_KEY;
    //String STRIPE_API_KEY = System.getenv().get("STRIPE_API_KEY");

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String STRIPE_WEBHOOK_SECRET;

    @PostMapping("/subscriptions/trial")
    String newSubscriptionWithTrial(@RequestBody RequestDTO requestDTO) throws Exception {
        Stripe.apiKey = STRIPE_API_KEY;

        //String clientBaseURL = System.getenv().get("CLIENT_BASE_URL");
        String clientBaseURL = "http://localhost:5173";

        // Find the user record from the database
        User user = userResource.getUserByEmail(requestDTO.getCustomerEmail());

        try {
            mutex.acquire();
            if (user != null && !user.isVerified()) {
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
                                .setAutomaticTax(
                                        SessionCreateParams.AutomaticTax.builder()
                                                .setEnabled(true)
                                                .build()
                                )
                                // Develop this section to lower the score
                                .setSubscriptionData(
                                        SessionCreateParams.SubscriptionData.builder()
                                                .setTrialPeriodDays(3L)
                                                .setDescription("Subscription to " + ProductDAO.getProduct(requestDTO.getItem()).getName() + " for " + user.getEmail() + " with a trial period of 3 days.")
                                                .build()
                                )
                                .setClientReferenceId(user.getId());

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
                        .setIdempotencyKey(user.getId())
                        .build();

                Session session = Session.create(paramsBuilder.build());

                return session.getUrl();

            } else if (user != null && user.isVerified()) {
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
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            // Invalid signature
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // Handle the event
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();

            // Fulfill the purchase...
            System.out.println("Checkout session completed: " + session.getCustomerEmail());
            System.out.println("Session ID:" + session.getClientReferenceId());
            User user = userResource.getUserByEmail(session.getCustomerEmail());
            user.setVerified(true);
        }

        return ResponseEntity.ok("Received");
    }
}