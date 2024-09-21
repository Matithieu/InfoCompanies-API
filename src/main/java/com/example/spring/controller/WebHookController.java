package com.example.spring.controller;

import com.example.spring.DTO.TierUser;
import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.RoleResource;
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
import static com.example.spring.utils.UserQuotaUtil.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/stripe")
public class WebHookController {

    @Autowired
    private UserResource userResource;

    @Autowired
    RoleResource roleResource;

    @Autowired
    UserQuotaService userQuotaService;

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String STRIPE_WEBHOOK_SECRET;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (JsonSyntaxException e) {
            System.out.println("⚠️  Webhook error while parsing basic request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        } catch (SignatureVerificationException e) {
            System.out.println("⚠️  Webhook error: invalid signature: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

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
            String tier = getTierBasedOnPriceId(stripePlanId);
            TierUser tierUser = getQuotaBasedOnTier(tier);

            Customer customer = retrieveCustomer(subscription.getCustomer());
            User user = userResource.getUserByEmail(customer.getEmail());
            user.setTier(tierUser);
            user.setVerified(true);
            user.setHasCompletedOnboarding(false);

            roleResource.addRoleToUser(user.getId(), "verified");
            userResource.updateUser(user);
            userQuotaService.createQuotaForUser(user.getId(), getRemainingSearchesBasedOnUserTier(user));
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
                    roleResource.removeRoleFromUser(user.getId(), "verified");
                    userResource.updateUser(user);
                    break;
                case "active":
                case "trialing":
                    user.setVerified(true);
                    roleResource.removeRoleFromUser(user.getId(), "verified");
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
            roleResource.removeRoleFromUser(user.getId(), "verified");
            user.setVerified(false);
            userResource.updateUser(user);
        } else {
            System.out.println("Failed to get subscription object");
        }
    }
}
