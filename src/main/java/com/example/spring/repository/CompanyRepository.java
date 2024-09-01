package com.example.spring.repository;

import com.example.spring.DTO.CompanyDetails;
import com.example.spring.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findCompanyById(Long id);

    @Query("SELECT new com.example.spring.DTO.CompanyDetails(c.id, c.companyName, c.industrySector, c.city, c.region) " +
            "FROM Company c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT(:companyName, '%'))")
    Page<CompanyDetails> findCompanyDetailsByCompanyName(@Param("companyName") String companyName, Pageable pageable);

    // Query without the numberOfEmployee filter
    @Query("SELECT c FROM Company c WHERE " +
            "(:regions IS NULL OR c.region IN :regions) AND " +
            "(:cities IS NULL OR c.city IN :cities) AND " +
            "(:industrySectors IS NULL OR c.industrySector IN :industrySectors) AND " +
            "(:legalForms IS NULL OR c.legalForm IN :legalForms) AND " +
            "(:includeLinkedin IS NULL OR :includeLinkedin = FALSE OR (c.linkedin IS NOT NULL AND c.linkedin <> '')) AND " +
            "(:includeYoutube IS NULL OR :includeYoutube = FALSE OR (c.youtube IS NOT NULL AND c.youtube <> '')) AND " +
            "(:includeFacebook IS NULL OR :includeFacebook = FALSE OR (c.facebook IS NOT NULL AND c.facebook <> '')) AND " +
            "(:includeInstagram IS NULL OR :includeInstagram = FALSE OR (c.instagram IS NOT NULL AND c.instagram <> '')) AND " +
            "(:includeTwitter IS NULL OR :includeTwitter = FALSE OR (c.twitter IS NOT NULL AND c.twitter <> '')) AND " +
            "(:includePhoneNumber IS NULL OR :includePhoneNumber = FALSE OR (c.phoneNumber IS NOT NULL AND c.phoneNumber <> '')) AND " +
            "(:includeEmail IS NULL OR :includeEmail = FALSE OR (c.email IS NOT NULL AND c.email <> '')) AND " +
            "(:includeWebsite IS NULL OR :includeWebsite = FALSE OR (c.website IS NOT NULL AND c.website <> '')) " +
            "ORDER BY c.phoneNumber")
    Page<Company> findCompaniesByFilters(@Param("regions") List<String> regions,
                                         @Param("cities") List<String> cities,
                                         @Param("industrySectors") List<String> industrySectors,
                                         @Param("legalForms") List<String> legalForms,
                                         @Param("includeLinkedin") Boolean includeLinkedin,
                                         @Param("includeYoutube") Boolean includeYoutube,
                                         @Param("includeFacebook") Boolean includeFacebook,
                                         @Param("includeInstagram") Boolean includeInstagram,
                                         @Param("includeTwitter") Boolean includeTwitter,
                                         @Param("includePhoneNumber") Boolean includePhoneNumber,
                                         @Param("includeEmail") Boolean includeEmail,
                                         @Param("includeWebsite") Boolean includeWebsite,
                                         Pageable pageable);


    @Query("SELECT c FROM Company c WHERE " +
            "(:regions IS NULL OR c.region IN :regions) AND " +
            "(:cities IS NULL OR c.city IN :cities) AND " +
            "(:industrySectors IS NULL OR c.industrySector IN :industrySectors) AND " +
            "(:legalForms IS NULL OR c.legalForm IN :legalForms) AND " +
            "(:includeLinkedin IS NULL OR :includeLinkedin = FALSE OR (c.linkedin IS NOT NULL AND c.linkedin <> '')) AND " +
            "(:includeYoutube IS NULL OR :includeYoutube = FALSE OR (c.youtube IS NOT NULL AND c.youtube <> '')) AND " +
            "(:includeFacebook IS NULL OR :includeFacebook = FALSE OR (c.facebook IS NOT NULL AND c.facebook <> '')) AND " +
            "(:includeInstagram IS NULL OR :includeInstagram = FALSE OR (c.instagram IS NOT NULL AND c.instagram <> '')) AND " +
            "(:includeTwitter IS NULL OR :includeTwitter = FALSE OR (c.twitter IS NOT NULL AND c.twitter <> '')) AND" +
            "(:includePhoneNumber IS NULL OR :includePhoneNumber = FALSE OR (c.phoneNumber IS NOT NULL AND c.phoneNumber <> '')) AND " +
            "(:includeEmail IS NULL OR :includeEmail = FALSE OR (c.email IS NOT NULL AND c.email <> '')) AND " +
            "(:includeWebsite IS NULL OR :includeWebsite = FALSE OR (c.website IS NOT NULL AND c.website <> '')) AND" +
            "((:comparator = '>' AND c.numberOfEmployee > :numberOfEmployee) OR " +
            " (:comparator = '<' AND c.numberOfEmployee < :numberOfEmployee) OR " +
            " (:comparator = '=' AND c.numberOfEmployee = :numberOfEmployee)) " +
            "ORDER BY c.phoneNumber")
    Page<Company> findCompaniesByFiltersWithEmployeeFilter(@Param("regions") List<String> regions,
                                                           @Param("cities") List<String> cities,
                                                           @Param("industrySectors") List<String> industrySectors,
                                                           @Param("legalForms") List<String> legalForms,
                                                           @Param("comparator") String comparator,
                                                           @Param("numberOfEmployee") Integer numberOfEmployee,
                                                           @Param("includeLinkedin") Boolean includeLinkedin,
                                                           @Param("includeYoutube") Boolean includeYoutube,
                                                           @Param("includeFacebook") Boolean includeFacebook,
                                                           @Param("includeInstagram") Boolean includeInstagram,
                                                           @Param("includeTwitter") Boolean includeTwitter,
                                                           @Param("includePhoneNumber") Boolean includePhoneNumber,
                                                           @Param("includeEmail") Boolean includeEmail,
                                                           @Param("includeWebsite") Boolean includeWebsite,
                                                           Pageable pageable);

    @Query(value = "SELECT * FROM companies WHERE companies.phone_number IS NOT NULL ORDER BY RANDOM()",
            countQuery = "SELECT COUNT(*) FROM companies WHERE companies.phone_number IS NOT NULL", nativeQuery = true)
    Page<Company> findRandomCompanies(Pageable pageable);

    Page<Company> findAllByIdIn(List<Long> ids, Pageable pageable);

    @Query(value = "SELECT c.* FROM companies c WHERE NOT EXISTS " +
            "(SELECT 1 FROM company_seen_company_ids csci JOIN company_seen cs ON csci.company_seen_id = cs.id " +
            "WHERE csci.company_ids = c.id AND cs.user_id = :userId) AND c.phone_number IS NOT NULL",
            countQuery = "SELECT COUNT(*) FROM companies c WHERE NOT EXISTS " +
                    "(SELECT 1 FROM company_seen_company_ids csci JOIN company_seen cs ON csci.company_seen_id = cs.id " +
                    "WHERE csci.company_ids = c.id AND cs.user_id = :userId) AND c.phone_number IS NOT NULL", nativeQuery = true)
    Page<Company> findRandomSeenCompanies(@Param("userId") String userId, Pageable pageable);
}