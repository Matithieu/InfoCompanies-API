package com.example.spring.app.company.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(nullable = true)
public class ContactDTO {
    String email;
    String phoneNumber;
    String website;
}
