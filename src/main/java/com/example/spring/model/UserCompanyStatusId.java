package com.example.spring.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class UserCompanyStatusId implements Serializable {

    private String userId;
    private Long companyId;

    public UserCompanyStatusId() {
    }

    public UserCompanyStatusId(String userId, Long companyId) {
        this.userId = userId;
        this.companyId = companyId;
    }
}
