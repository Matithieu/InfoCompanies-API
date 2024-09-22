package com.example.spring.controller;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.RoleResource;
import com.example.spring.keycloakClient.UserResource;
import com.example.spring.service.UserQuotaService;
import com.example.spring.utils.CustomerUtil;
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

import static com.example.spring.utils.HeadersUtil.parseEmailFromHeader;
import static com.example.spring.utils.HeadersUtil.parseUserFromHeader;

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
        String userId = parseUserFromHeader();
        //System.out.println("Headers: " + getAllHeaders());

        // Find the user record from the database
        User user = userResource.getUserById(userId);

        try {
            if (user != null && !user.isVerified()) {
                System.out.println("User trying to subscribe: " + user.getEmail());
                Customer customer = CustomerUtil.findOrCreateCustomer(user);

                // Next, create a checkout session by adding the details of the checkout
                SessionCreateParams.Builder paramsBuilder =
                        SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                                .setSuccessUrl(clientBaseURL + "/completion?session_id={CHECKOUT_SESSION_ID}")
                                .setCancelUrl(clientBaseURL + "/failure")
                                .setCustomer(customer.getId())
                                .setCustomerUpdate(
                                        SessionCreateParams.CustomerUpdate.builder()
                                                .setName(SessionCreateParams.CustomerUpdate.Name.AUTO)
                                                .setAddress(SessionCreateParams.CustomerUpdate.Address.AUTO)
                                                .build()
                                )
                                .setClientReferenceId(user.getId())
                                .setAllowPromotionCodes(true)
                                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                                .setPaymentMethodCollection(SessionCreateParams.PaymentMethodCollection.IF_REQUIRED)
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

                if (Objects.equals(priceId, STRIPE_PRICE_ID_FREE)) {
                    paramsBuilder.setSubscriptionData(
                            SessionCreateParams.SubscriptionData.builder()
                                    .setTrialSettings(
                                            SessionCreateParams.SubscriptionData.TrialSettings.builder()
                                                    .setEndBehavior(
                                                            SessionCreateParams.SubscriptionData.TrialSettings.EndBehavior.builder()
                                                                    .setMissingPaymentMethod(
                                                                            SessionCreateParams.SubscriptionData.TrialSettings.EndBehavior.MissingPaymentMethod.CANCEL
                                                                    ).build()
                                                    ).build()
                                    ).build()
                    );
                }

                RequestOptions requestOptions = RequestOptions.builder()
                        .setIdempotencyKey(user.getId())
                        .build();

                Session session = Session.create(paramsBuilder.build());

                return ResponseEntity.ok(session.getUrl());

            } else if (user != null && user.isVerified()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already verified");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (StripeException e) {
            throw new Exception("Error when fetching /subscriptions/trial ", e);
        }
    }
}