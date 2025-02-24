package com.example.spring.controller;

import com.example.spring.enums.Status;
import com.example.spring.service.UserCompanyStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.spring.utils.HeadersUtil.parseUserIdFromHeader;

@RestController
@RequestMapping("/v1/companies-status")
public class UserCompanyStatusController {

    @Autowired
    private UserCompanyStatusService userCompanyStatusService;

    @PostMapping("/{companyId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long companyId,
                                          @RequestBody Map<String, String> body) {

        String userId = parseUserIdFromHeader();
        String statusValue = body.get("status");
        Status status = Status.valueOf(statusValue);

        userCompanyStatusService.updateCompanyStatus(userId, companyId, status);
        return ResponseEntity.ok().build();
    }
}
