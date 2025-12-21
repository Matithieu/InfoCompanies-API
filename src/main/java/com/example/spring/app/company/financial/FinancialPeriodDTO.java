package com.example.spring.app.company.financial;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "FinancialPeriod")
public class FinancialPeriodDTO {
    private FinancialYear year;
    private FinancialPeriod period;
    private LocalDate closingDate;
    private Double revenue;
    private Double turnover;
}
