package com.example.spring.controller;

import com.example.spring.DTO.TierUser;
import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.RoleResource;
import com.example.spring.keycloakClient.UserResource;
import com.example.spring.service.UserQuotaService;
import com.example.spring.utils.LogUtil;
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

import java.util.Map;

import static com.example.spring.utils.CustomerUtil.retrieveCustomerById;
import static com.example.spring.utils.UserQuotaUtil.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/stripe")
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
            LogUtil.warn("⚠️ Webhook error while parsing basic request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        } catch (SignatureVerificationException e) {
            LogUtil.warn("⚠️ Webhook error: invalid signature: " + e.getMessage());
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
                LogUtil.warn("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok("Received");
    }

    private void handleChargeSucceeded(Event event) {
        LogUtil.info("Charge succeeded: ", Map.of(
                "event_id", event.getId()
        ));
    }

    private void handleSubscriptionCreated(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
            Subscription subscription = (Subscription) stripeObject;

            LogUtil.info("Subscription created: ", Map.of(
                    "subscription_id", subscription.getId(),
                    "customer_id", subscription.getCustomer()
            ));

            String stripePlanId = subscription.getItems().getData().getFirst().getPlan().getId();
            String tier = getTierBasedOnPriceId(stripePlanId);
            TierUser tierUser = getQuotaBasedOnTier(tier);

            Customer customer = retrieveCustomerById(subscription.getCustomer());
            User user = userResource.getUserByEmail(customer.getEmail());
            user.setTier(tierUser);
            user.setVerified(true);
            user.setHasCompletedOnboarding(false);

            roleResource.addRoleToUser(user.getId(), "verified");
            userResource.updateUser(user);
            userQuotaService.createQuotaForUser(user.getId(), getRemainingSearchesBasedOnUserTier(user));
        } else {
            LogUtil.warn("Sub Created: Failed to get subscription object for event: " + event.getId());
        }
    }

    private void handleSubscriptionUpdated(Event event) throws StripeException {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
        if (subscription != null) {
            LogUtil.info("Sub Updated: ", Map.of(
                    "subscription_id", subscription.getId(),
                    "customer_id", subscription.getCustomer(),
                    "status", subscription.getStatus()
            ));

            Customer customer = retrieveCustomerById(subscription.getCustomer());
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
                    LogUtil.warn("Sub Updated: Unhandled event type: " + event.getType());
                    break;
            }
        } else {
            LogUtil.warn("Sub Updated: Failed to get subscription object for event: " + event.getId());
        }
    }

    public void handleSubscriptionDeleted(Event event) throws StripeException {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
        if (subscription != null) {
            LogUtil.info("Subscription deleted: ", Map.of(
                    "subscription_id", subscription.getId(),
                    "customer_id", subscription.getCustomer()
            ));

            Customer customer = retrieveCustomerById(subscription.getCustomer());
            User user = userResource.getUserByEmail(customer.getEmail());
            roleResource.removeRoleFromUser(user.getId(), "verified");
            user.setVerified(false);
            userResource.updateUser(user);
        } else {
            LogUtil.warn("Sub Deleted: Failed to get subscription object");
        }
    }
}
