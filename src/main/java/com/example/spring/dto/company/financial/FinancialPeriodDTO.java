package com.example.spring.dto.company.financial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialPeriodDTO {
    private FinancialYear year;
    private FinancialPeriod period;
    private LocalDate closingDate;
    private Double revenue;
    private Double turnover;
}
