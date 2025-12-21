package com.example.spring.app.company.dto;

import com.example.spring.app.userCompanyStatus.UserCompanyStatusModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CompanyWithStatus")
public class CompanyDtoWithStatusDTO implements Serializable {
    private CompanyDTO companyDTO;

    @Schema(nullable = true)
    private UserCompanyStatusModel userCompanyStatus;
}
