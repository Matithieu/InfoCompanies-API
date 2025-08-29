package com.example.spring.controller;

import com.example.spring.enums.Status;
import com.example.spring.model.UserCompanyStatus;
import com.example.spring.service.UserCompanyStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.spring.utils.HeadersUtil.parseUserIdFromHeader;

@RestController
@RequestMapping("/v1/companies-status")
public class UserCompanyStatusController {

    @Autowired
    private UserCompanyStatusService userCompanyStatusService;

    @PostMapping("/update-status")
    public ResponseEntity<UserCompanyStatus> updateStatus(@RequestParam Integer companyId,
                                                          @RequestParam Status status) {
        String userId = parseUserIdFromHeader();
        UserCompanyStatus updated = userCompanyStatusService.updateCompanyStatus(userId, companyId, status);

        if (updated == null) {
            return ResponseEntity.noContent().build(); // deleted or no-op
        }

        return ResponseEntity.ok(updated);
    }
}
