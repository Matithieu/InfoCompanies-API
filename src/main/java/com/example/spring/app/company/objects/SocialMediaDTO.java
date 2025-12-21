package com.example.spring.app.company.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@Schema(nullable = true)
public class SocialMediaDTO {
    private String instagram;
    private String facebook;
    private String twitter;
    private String linkedin;
    private String youtube;
}
