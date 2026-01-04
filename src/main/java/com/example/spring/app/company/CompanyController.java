package com.example.spring.app.company;

import com.example.spring.app.company.dto.CompanyDTO;
import com.example.spring.app.company.dto.CompanyDetails;
import com.example.spring.app.company.dto.CompanyDtoWithStatusDTO;
import com.example.spring.app.company.dto.CompanyFilterRequest;
import com.example.spring.app.company.enums.Status;
import com.example.spring.app.userCompanyStatus.UserCompanyStatusModel;
import com.example.spring.app.userCompanyStatus.UserCompanyStatusService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.spring.common.utils.JwtUtil.extractUserIdFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/v1/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final UserCompanyStatusService userCompanyStatusService;

    public CompanyController(CompanyService companyService,
                             UserCompanyStatusService userCompanyStatusService) {
        this.companyService = companyService;
        this.userCompanyStatusService = userCompanyStatusService;
    }

    // Example: http://localhost:8080/api/v1/company/search-by-name?companyName=ExampleCompany&page=0
    @GetMapping("/")
    public Page<CompanyDetails> searchCompaniesByName(@RequestParam("companyName") String companyName,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return companyService.searchCompanies(companyName, pageable);
    }

    @GetMapping("/{id}")
    public CompanyDtoWithStatusDTO getCompanyById(@PathVariable("id") Integer id) {
        String userId = extractUserIdFromHeader();
        CompanyDTO companyDto = companyService.getCompanyById(id).toCompanyDTO();
        UserCompanyStatusModel userCompanyStatus = userCompanyStatusService
                .getOneUserCompanyStatusByUserIdAndCompanyId(userId, id);

        return CompanyUtil.fillCompanyDtoWithStatusDto(companyDto, userCompanyStatus);
    }

    // Example: http://localhost:8080/api/v1/company/get-seen-by-user?page=0
    @GetMapping("/seen")
    public Page<CompanyDtoWithStatusDTO> getCompaniesSeenByUser(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        String userId = extractUserIdFromHeader();
        Page<CompanyModel> companies = companyService.getCompaniesSeenByUser(userId, pageable);

        List<UserCompanyStatusModel> userCompanyStatuses = userCompanyStatusService
                .getMultipleUserCompanyStatusByUserIdAndCompanyIds(userId, companies.getContent()
                        .stream()
                        .map(CompanyModel::getId)
                        .toList());

        return CompanyUtil.fillPaginationCompanyDtoWithStatusDto(companies, userCompanyStatuses);
    }

    // Example: http://localhost:8080/api/v1/company/filter-by-parameters?regions=region1,region2&cities=city1,city2&industrySectors=sector1,sector2&legalForms=form1,form2&page=0
    @PostMapping("/filter")
    public Page<CompanyDtoWithStatusDTO> getCompaniesByFilters(
            @RequestBody(required = false) CompanyFilterRequest filterRequest) {
        String userId = extractUserIdFromHeader();
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());

        Page<CompanyModel> companies = companyService.findCompaniesByFilters(
                filterRequest.getRegionNames(),
                filterRequest.getCityNames(),
                filterRequest.getIndustrySectorNames(),
                filterRequest.getLegalFormNames(),
                filterRequest.getNumberOfEmployeeFilter(),
                filterRequest.getSocials(),
                filterRequest.getContacts(),
                filterRequest.getIsCompanySeen(),
                userId,
                pageable
        );

        List<UserCompanyStatusModel> userCompanyStatuses = userCompanyStatusService
                .getMultipleUserCompanyStatusByUserIdAndCompanyIds(userId, companies.getContent()
                        .stream()
                        .map(CompanyModel::getId)
                        .toList());

        return CompanyUtil.fillPaginationCompanyDtoWithStatusDto(companies, userCompanyStatuses);
    }

    // Example: http://localhost:8080/api/v1/company/random-unseen?page=0
    @GetMapping("/random")
    public Page<CompanyDtoWithStatusDTO> getRandomUnseenCompanies(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        String userId = extractUserIdFromHeader();
        Page<CompanyModel> companies = companyService.findRandomUnseenCompanies(userId, pageable);
        List<UserCompanyStatusModel> userCompanyStatuses = userCompanyStatusService
                .getMultipleUserCompanyStatusByUserIdAndCompanyIds(userId, companies.getContent()
                        .stream()
                        .map(CompanyModel::getId)
                        .toList());

        return CompanyUtil.fillPaginationCompanyDtoWithStatusDto(companies, userCompanyStatuses);
    }

    // Make a request to the scrap API
    // Example: http://localhost:8080/api/v1/company/scrap?companyId=1
    @GetMapping("/{id}/scrap")
    public CompanyDTO scrapCompany(@PathVariable("id") Integer companyId) {
        CompanyModel company = companyService.getCompanyById(companyId);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }

        boolean needsScraping = company.getScrapingDate() == null ||
                company.getScrapingDate().isBefore(LocalDate.now().minusDays(1));

        if (!needsScraping) {
            throw new ResponseStatusException(HttpStatus.TOO_EARLY, "The company was scrapped less than 1 day ago");
        }

        CompanyModel companyScraped = companyService.scrapCompany(company);
        company.updateFrom(companyScraped);
        companyService.saveCompany(company);

        return company.toCompanyDTO();
    }


    // Example: http://localhost:8080/api/v1/company/filter-by-parameters?regions=region1,region2&cities=city1,city2&industrySectors=sector1,sector2&legalForms=form1,form2&page=0
    @GetMapping("/landing")
    public Page<CompanyDTO> getCompaniesOnLandingByFilters(
            //@RequestParam(required = false) List<String> regions,
            @RequestParam(required = false) List<String> cityNames,
            @RequestParam(required = false) List<String> industrySectorNames
            //@RequestParam(required = false) List<String> legalForms,
            //@RequestParam(required = false) String comparator,
            //@RequestParam(required = false) Integer numberOfEmployee
    ) {

        Pageable pageable = PageRequest.of(0, 10);

        // First try to get companies that the user has not seen yet
        Page<CompanyModel> companiesPage = companyService.findCompaniesByFilters(null, cityNames, industrySectorNames, null,
                null, null, null, false, null, pageable);

        // If there are not enough unseen companies, get seen companies to fill the page
        if (companiesPage.getTotalElements() < 7) {
            companiesPage = companyService.findCompaniesByFilters(null, cityNames, industrySectorNames, null,
                    null, null, null, true, null, pageable);
        }

        return new PageImpl<>(
                companiesPage.getContent().stream()
                        .peek(CompanyModel::obstructCompany)
                        .map(CompanyModel::toCompanyDTO)
                        .collect(Collectors.toList()),
                companiesPage.getPageable(),
                companiesPage.getTotalElements()
        );
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<UserCompanyStatusModel> updateStatus(@PathVariable("id") Integer companyId,
                                                               @RequestParam Status status) {
        String userId = extractUserIdFromHeader();
        UserCompanyStatusModel updated = userCompanyStatusService.updateCompanyStatus(userId, companyId, status);

        if (updated == null) {
            return ResponseEntity.noContent().build(); // deleted or no-op
        }

        return ResponseEntity.ok(updated);
    }
}
