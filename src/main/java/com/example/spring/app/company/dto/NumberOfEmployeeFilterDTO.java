package com.example.spring.app.company.dto;

import com.example.spring.app.filters.autocomplete.SignComparator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(nullable = true)
public class NumberOfEmployeeFilterDTO {
    Integer numberOfEmployee;
    SignComparator signComparator;
}
