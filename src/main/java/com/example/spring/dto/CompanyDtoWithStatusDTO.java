package com.example.spring.dto;

import com.example.spring.dto.company.CompanyDTO;
import com.example.spring.model.UserCompanyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDtoWithStatusDTO implements Serializable {
    private CompanyDTO companyDTO;

    @Schema(nullable = true)
    private UserCompanyStatus userCompanyStatus;
}
