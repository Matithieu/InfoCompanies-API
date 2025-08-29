package com.example.spring.dto;

import com.example.spring.enums.SignComparator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(nullable = true)
public class NumberOfEmployeeFilter {
    Integer numberOfEmployee;
    SignComparator signComparator;
}
