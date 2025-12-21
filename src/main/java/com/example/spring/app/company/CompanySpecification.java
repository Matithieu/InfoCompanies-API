package com.example.spring.app.company;

import com.example.spring.app.userCompanyStatus.UserCompanyStatusModel;
import com.example.spring.app.company.dto.NumberOfEmployeeFilterDTO;
import com.example.spring.app.company.objects.ContactDTO;
import com.example.spring.app.company.objects.SocialMediaDTO;
import com.example.spring.app.filters.autocomplete.SignComparator;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CompanySpecification {

    public static Specification<CompanyModel> regionsIn(List<String> regionNames) {
        return (root, query, builder) ->
                (regionNames == null || regionNames.isEmpty()) ? null : root.get("region").in(regionNames);
    }

    public static Specification<CompanyModel> citiesIn(List<String> cityNames) {
        return (root, query, builder) ->
                (cityNames == null || cityNames.isEmpty()) ? null : root.get("city").in(cityNames);
    }

    public static Specification<CompanyModel> industrySectorsIn(List<String> industrySectorNames) {
        return (root, query, builder) ->
                (industrySectorNames == null || industrySectorNames.isEmpty()) ? null : root.get("industrySector").in(industrySectorNames);
    }

    public static Specification<CompanyModel> legalFormsIn(List<String> legalFormNames) {
        return (root, query, builder) ->
                (legalFormNames == null || legalFormNames.isEmpty()) ? null : root.get("legalForm").in(legalFormNames);
    }

    public static Specification<CompanyModel> employeeComparator(NumberOfEmployeeFilterDTO numberOfEmployeeFilter) {
        return (root, query, builder) -> {
            if (numberOfEmployeeFilter == null) {
                return null;
            }

            Integer numberOfEmployee = numberOfEmployeeFilter.getNumberOfEmployee();
            SignComparator signComparator = numberOfEmployeeFilter.getSignComparator();


            return switch (signComparator) {
                case SignComparator.LOWER_THAN -> builder.lessThan(root.get("numberOfEmployee"), numberOfEmployee);
                case SignComparator.GREATER_THAN -> builder.greaterThan(root.get("numberOfEmployee"), numberOfEmployee);
                case SignComparator.EQUAL -> builder.equal(root.get("numberOfEmployee"), numberOfEmployee);
            };
        };
    }

    public static Specification<CompanyModel> socialMediaNotNull(SocialMediaDTO socials) {
        return (root, query, builder) -> {
            if (socials == null) {
                return null;
            }

            Predicate predicate = builder.conjunction();
            if (socials.getLinkedin() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("linkedin")));
            }
            if (socials.getYoutube() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("youtube")));
            }
            if (socials.getFacebook() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("facebook")));
            }
            if (socials.getInstagram() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("instagram")));
            }
            if (socials.getTwitter() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("twitter")));
            }
            return predicate;
        };
    }

    public static Specification<CompanyModel> contactInfoNotNull(ContactDTO contacts) {
        return (root, query, builder) -> {
            if (contacts == null) {
                return null;
            }

            Predicate predicate = builder.conjunction();
            if (contacts.getPhoneNumber() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("phoneNumber")));
            }
            if (contacts.getEmail() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("email")));
            }
            if (contacts.getWebsite() != null) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("website")));
            }
            return predicate;
        };
    }

    public static Specification<CompanyModel> notSeenByUser(Boolean isCompanySeen, String userId) {
        return (root, query, builder) -> {
            if (isCompanySeen == null || userId == null) {
                return null;
            }

            // Create subquery to select company IDs that have been seen by the user
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<UserCompanyStatusModel> userCompanyStatusRoot = subquery.from(UserCompanyStatusModel.class);
            subquery.select(userCompanyStatusRoot.get("companyId"))
                    .where(builder.equal(userCompanyStatusRoot.get("userId"), userId));

            // Return companies not in the subquery result (i.e., companies not seen by the user)
            return builder.not(root.get("id").in(subquery));
        };
    }
}
