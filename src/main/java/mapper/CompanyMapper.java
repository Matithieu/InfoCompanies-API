package mapper;

import com.example.spring.dto.company.CompanyDTO;
import com.example.spring.model.Company;

import java.util.List;

import static mapper.FinancialPeriodMapper.toFinancialPeriodDTOList;


public class CompanyMapper {

    public static CompanyDTO toCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .sirenNumber(company.getSirenNumber())
                .nicNumber(company.getNicNumber())
                .legalForm(company.getLegalForm())
                .apeCode(company.getApeCode())
                .apeLabel(company.getApeLabel())
                .address(company.getAddress())
                .postalCode(company.getPostalCode())
                .departmentNumber(company.getDepartmentNumber())
                .department(company.getDepartment())
                .city(company.getCity())
                .region(company.getRegion())
                .tradeName(company.getTradeName())
                .registrationDate(company.getRegistrationDate())
                .deregistrationDate(company.getDeregistrationDate())
                .industrySector(company.getIndustrySector())
                .phoneNumber(company.getPhoneNumber())
                .website(company.getWebsite())
                .reviews(company.getReviews())
                .schedule(company.getSchedule())
                .instagram(company.getInstagram())
                .facebook(company.getFacebook())
                .twitter(company.getTwitter())
                .linkedin(company.getLinkedin())
                .youtube(company.getYoutube())
                .email(company.getEmail())
                .scrapingDate(company.getScrapingDate())
                .dateCreation(company.getDateCreation())
                .lastProcessingDate(company.getLastProcessingDate())
                .numberOfEmployee(company.getNumberOfEmployee())
                .companyCategory(company.getCompanyCategory())
                .financialPeriods(toFinancialPeriodDTOList(company))
                .build();
    }

    public static List<CompanyDTO> toCompanyDTOList(List<Company> companies) {
        return companies.stream()
                .map(CompanyMapper::toCompanyDTO)
                .toList();
    }
}