package com.example.spring.controller;

import com.example.spring.DTO.QuotaUser;
import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.RoleResource;
import com.example.spring.keycloakClient.UserResource;
import com.example.spring.service.UserQuotaService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.example.spring.utils.CustomerUtil.retrieveCustomer;
import static com.example.spring.utils.HeadersUtil.getAllHeaders;
import static com.example.spring.utils.HeadersUtil.parseEmailFromHeader;
import static com.example.spring.utils.UserQuotaUtil.*;

// https://kinsta.com/blog/stripe-java-api/

@RestController
@CrossOrigin
@RequestMapping("/v1/stripe")
public class PaymentController {

    @Autowired
    private UserResource userResource;

    @Autowired
    RoleResource roleResource;

    @Autowired
    UserQuotaService userQuotaService;

    @Value("${STRIPE_API_KEY}")
    private String STRIPE_API_KEY;

    @Value("${HOSTNAME}")
    private String HOSTNAME;

    @Value("${STRIPE_PRICE_ID_FREE}")
    private String STRIPE_PRICE_ID_FREE;

    @PostMapping("/subscriptions/trial")
    public ResponseEntity<String> newSubscriptionWithTrial(HttpServletRequest request) throws Exception {
        Stripe.apiKey = STRIPE_API_KEY;

        String clientBaseURL = "https://" + HOSTNAME + "/ui";
        String priceId = request.getHeader("X-priceId");
        String email = parseEmailFromHeader();
        //System.out.println("Headers: " + getAllHeaders());

        // Find the user record from the database
        User user = userResource.getUserByEmail(email);

        try {
            if (user != null && !user.isVerified()) {
                // Start by finding existing customer record from Stripe or creating a new one if needed
                //Customer customer = CustomerUtil.findOrCreateCustomer(user);

                System.out.println("User trying to subscribe: " + user.getEmail());

                // Temporary function for free user
                if (Objects.equals(priceId, STRIPE_PRICE_ID_FREE)) {
                    String tierUser = getTierBasedOnPriceId(priceId);
                    QuotaUser quotaUser = getQuotaBasedOnTier(tierUser);

                    user.setTier(quotaUser);
                    user.setVerified(true);

                    roleResource.addRoleToUser(user.getId(), "verified");
                    userResource.updateUser(user);
                    userQuotaService.createQuotaForUser(user.getId(), getRemainingSearchesBasedOnUserTier(user));

                    // Redicrect to the dashboard
                    return ResponseEntity.ok(clientBaseURL + "/dashboard");
                }

                // Next, create a checkout session by adding the details of the checkout
                SessionCreateParams.Builder paramsBuilder =
                        SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                                .setSuccessUrl(clientBaseURL + "/completion?session_id={CHECKOUT_SESSION_ID}")
                                .setCancelUrl(clientBaseURL + "/failure")
                                //.setCustomer(customer.getId())
                                .setClientReferenceId(user.getId())
                                .setAllowPromotionCodes(true)
                                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                                // Develop this section to lower the score
                                .putMetadata("user_id", user.getId())
                                .putMetadata("email", user.getEmail())
                                .setSubscriptionData(
                                        SessionCreateParams.SubscriptionData.builder()
                                                .setTrialPeriodDays(3L)
                                                .setDescription("Subscription for " + user.getEmail() + " with a trial period of 3 days.")
                                                .build()
                                )
                                .setClientReferenceId(user.getId())
                                // Add the all the details to the session creation request
                                .addLineItem(
                                        SessionCreateParams.LineItem.builder()
                                                .setQuantity(1L)
                                                .setPrice(priceId)
                                                .build()
                                )
                                .setLocale(SessionCreateParams.Locale.AUTO)
                                .setPhoneNumberCollection(
                                        SessionCreateParams.PhoneNumberCollection.builder()
                                                .setEnabled(true)
                                                .build()
                                )
                                .setBillingAddressCollection(
                                        SessionCreateParams.BillingAddressCollection.REQUIRED
                                )
                                .setTaxIdCollection(
                                        SessionCreateParams.TaxIdCollection.builder()
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
        }
    }
}