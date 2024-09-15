package com.example.spring.model;

import com.example.spring.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "user_company_status")
@IdClass(UserCompanyStatusId.class)
@Getter
@Setter
public class UserCompanyStatus implements Serializable {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "company_id")
    private Long companyId;

    @Enumerated(EnumType.STRING)
    private Status status;
}
