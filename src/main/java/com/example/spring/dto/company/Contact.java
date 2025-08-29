package com.example.spring.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(nullable = true)
public class Contact {
    String email;
    String phoneNumber;
    String website;
}
