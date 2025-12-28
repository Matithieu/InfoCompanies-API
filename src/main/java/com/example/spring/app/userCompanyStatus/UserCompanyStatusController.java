package com.example.spring.app.userCompanyStatus;

import com.example.spring.app.company.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.spring.common.utils.JwtUtil.extractUserIdFromToken;

@RestController
@RequestMapping("/v1/companies-status")
public class UserCompanyStatusController {

    @Autowired
    private UserCompanyStatusService userCompanyStatusService;

    @PostMapping("/update-status")
    public ResponseEntity<UserCompanyStatusModel> updateStatus(@RequestParam Integer companyId,
                                                               @RequestParam Status status) {
        String userId = extractUserIdFromToken();
        UserCompanyStatusModel updated = userCompanyStatusService.updateCompanyStatus(userId, companyId, status);

        if (updated == null) {
            return ResponseEntity.noContent().build(); // deleted or no-op
        }

        return ResponseEntity.ok(updated);
    }
}
