package com.example.spring.specification;

import com.example.spring.model.Company;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CompanySpecification {

    public static Specification<Company> regionIn(List<String> regions) {
        return (root, query, builder) ->
                (regions == null || regions.isEmpty()) ? null : root.get("region").in(regions);
    }

    public static Specification<Company> cityIn(List<String> cities) {
        return (root, query, builder) ->
                (cities == null || cities.isEmpty()) ? null : root.get("city").in(cities);
    }

    public static Specification<Company> industrySectorIn(List<String> industrySectors) {
        return (root, query, builder) ->
                (industrySectors == null || industrySectors.isEmpty()) ? null : root.get("industrySector").in(industrySectors);
    }

    public static Specification<Company> legalFormIn(List<String> legalForms) {
        return (root, query, builder) ->
                (legalForms == null || legalForms.isEmpty()) ? null : root.get("legalForm").in(legalForms);
    }

    public static Specification<Company> employeeComparator(String comparator, Integer numberOfEmployee) {
        return (root, query, builder) -> {
            if (comparator == null || numberOfEmployee == null) {
                return null;  // Instead of builder.conjunction()
            }
            return switch (comparator) {
                case ">" -> builder.greaterThan(root.get("numberOfEmployee"), numberOfEmployee);
                case "<" -> builder.lessThan(root.get("numberOfEmployee"), numberOfEmployee);
                case "=" -> builder.equal(root.get("numberOfEmployee"), numberOfEmployee);
                default -> null;
            };
        };
    }

    public static Specification<Company> socialMediaNotNull(List<String> socials) {
        return (root, query, builder) -> {
            if (socials == null || socials.isEmpty()) {
                return builder.conjunction();
            }
            Predicate predicate = builder.conjunction();
            if (socials.contains("linkedin")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("linkedin")));
            }
            if (socials.contains("youtube")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("youtube")));
            }
            if (socials.contains("facebook")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("facebook")));
            }
            if (socials.contains("instagram")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("instagram")));
            }
            if (socials.contains("twitter")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("twitter")));
            }
            return predicate;
        };
    }

    public static Specification<Company> contactInfoNotNull(List<String> contacts) {
        return (root, query, builder) -> {
            if (contacts == null || contacts.isEmpty()) {
                return builder.conjunction();
            }
            Predicate predicate = builder.conjunction();
            if (contacts.contains("phone")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("phoneNumber")));
            }
            if (contacts.contains("email")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("email")));
            }
            if (contacts.contains("website")) {
                predicate = builder.and(predicate, builder.isNotNull(root.get("website")));
            }
            return predicate;
        };
    }
}
