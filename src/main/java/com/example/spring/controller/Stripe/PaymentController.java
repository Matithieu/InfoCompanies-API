package com.example.spring.controller.Stripe;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Semaphore;

import static com.example.spring.security.utils.SecurityUtils.getAllHeaders;
import static com.example.spring.security.utils.SecurityUtils.parseEmailFromHeader;

// https://kinsta.com/blog/stripe-java-api/

@RestController
@CrossOrigin
@RequestMapping("/v1/stripe")
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
    public ResponseEntity<String> newSubscriptionWithTrial(HttpServletRequest request) throws Exception {
        Stripe.apiKey = STRIPE_API_KEY;

        String clientBaseURL = "http://localhost/ui";
        String priceId = request.getHeader("X-priceId");
        String email = parseEmailFromHeader();
        System.out.println("Headers: " + getAllHeaders());

        // Find the user record from the database
        User user = userResource.getUserByEmail(email);

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
                                .setSuccessUrl(clientBaseURL + "/completion?session_id={CHECKOUT_SESSION_ID}")
                                .setCancelUrl(clientBaseURL + "/failure")
                                .setCustomer(customer.getId())
                                // Develop this section to lower the score
                                .setSubscriptionData(
                                        SessionCreateParams.SubscriptionData.builder()
                                                .setTrialPeriodDays(3L)
                                                .setDescription("Subscription for " + user.getEmail() + " with a trial period of 3 days.")
                                                .build()
                                )
                                .setClientReferenceId(user.getId());

                // Add the all the details to the session creation request
                paramsBuilder
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPrice(priceId)
                                        .build()
                        )
                        .setPhoneNumberCollection(
                                SessionCreateParams.PhoneNumberCollection.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .setCustomText(
                                SessionCreateParams.CustomText.builder()
                                        .setSubmit(SessionCreateParams.CustomText.Submit.builder()
                                                .setMessage("You can refund freely during 14 days.")
                                                .build()
                                        ).build()
                        );

                RequestOptions requestOptions = RequestOptions.builder()
                        .setIdempotencyKey(user.getId())
                        .build();

                Session session = Session.create(paramsBuilder.build());

                return ResponseEntity.ok(session.getUrl());

            } else if (user != null && user.isVerified()) {
                return ResponseEntity.ok("User is already verified");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (StripeException e) {
            throw new Exception("Error when fetching /subscriptions/trial ", e);
        } finally {
            mutex.release();
        }
    }
}