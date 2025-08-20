package com.example.spring.dto;

import com.example.spring.dto.company.CompanyDTO;
import com.example.spring.model.UserCompanyStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDtoWithStatusDTO implements Serializable {
    private CompanyDTO companyDTO;
    private UserCompanyStatus userCompanyStatus;
}
