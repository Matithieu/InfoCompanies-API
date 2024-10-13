package com.example.spring.DTO;

import com.example.spring.model.Company;
import com.example.spring.model.UserCompanyStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWithStatusDTO implements Serializable {
    private Company company;
    private UserCompanyStatus userCompanyStatus;
}
