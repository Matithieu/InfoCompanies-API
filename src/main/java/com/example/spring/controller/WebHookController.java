package com.example.spring.controller;

import com.example.spring.DTO.QuotaUser;
import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import com.example.spring.service.UserQuotaService;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.spring.utils.CustomerUtil.retrieveCustomer;
import static com.example.spring.utils.UserQuotaUtil.getQuotaBasedOnTier;
import static com.example.spring.utils.UserQuotaUtil.getTierBasedOnPriceId;

@RestController
@CrossOrigin
@RequestMapping("/v1/stripe")
public class WebHookController {

    @Autowired
    private UserResource userResource;

    @Autowired
    UserQuotaService userQuotaService;

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String STRIPE_WEBHOOK_SECRET;

    @Value("${STRIPE_API_KEY}")
    private String STRIPE_API_KEY;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (JsonSyntaxException e) {
            System.out.println("⚠️  Webhook error while parsing basic request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        } catch (SignatureVerificationException e) {
            System.out.println("⚠️  Webhook error: invalid signature.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // System.out.println("API VERSION" + event.getApiVersion());
        // System.out.println("STRIPE VERSION" + Stripe.API_VERSION);

        switch (event.getType()) {
            case "charge.succeeded":
                handleChargeSucceeded(event);
                break;
            case "customer.subscription.created":
                handleSubscriptionCreated(event);
                break;
            case "customer.subscription.updated":
                handleSubscriptionUpdated(event);
                break;
            case "customer.subscription.deleted":
                handleSubscriptionDeleted(event);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok("Received");
    }

    private void handleChargeSucceeded(Event event) {
        System.out.println("Charge succeeded: " + event);
    }

    private void handleSubscriptionCreated(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
            Subscription subscription = (Subscription) stripeObject;
            System.out.println("Subscription created: " + subscription.getCustomer());

            String stripePlanId = subscription.getItems().getData().getFirst().getPlan().getId();
            String tierUser = getTierBasedOnPriceId(stripePlanId);
            QuotaUser quotaUser = getQuotaBasedOnTier(tierUser);

            Customer customer = retrieveCustomer(subscription.getCustomer());
            User user = userResource.getUserByEmail(customer.getEmail());
            user.setTier(quotaUser);
            user.setVerified(true);

            userResource.updateUser(user);
            userQuotaService.updateQuotaForUser(user.getId(), quotaUser.getRemainingSearchesBasedOnUserTier(user));
        } else {
            System.out.println("Failed to get subscription object");
        }
    }

    private void handleSubscriptionUpdated(Event event) throws StripeException {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
        if (subscription != null) {
            System.out.println("Subscription updated: " + subscription.getCustomer());
            Customer customer = retrieveCustomer(subscription.getCustomer());
            User user = userResource.getUserByEmail(customer.getEmail());

            switch (subscription.getStatus()) {
                case "canceled":
                case "paused":
                case "unpaid":
                    user.setVerified(false);
                    userResource.updateUser(user);
                    break;
                case "active":
                case "trialing":
                    user.setVerified(true);
                    userResource.updateUser(user);
                    break;
                case null:
                default:
                    System.out.println("Unhandled event type: " + event.getType());
                    break;
            }
        } else {
            System.out.println("Failed to get subscription object");
        }
    }

    public void handleSubscriptionDeleted(Event event) throws StripeException {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
        if (subscription != null) {
            System.out.println("Subscription deleted: " + subscription.getCustomer());
            Customer customer = retrieveCustomer(subscription.getCustomer());
            User user = userResource.getUserByEmail(customer.getEmail());
            user.setVerified(false);
            userResource.updateUser(user);
        } else {
            System.out.println("Failed to get subscription object");
        }
    }
}
