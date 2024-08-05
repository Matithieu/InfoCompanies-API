package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Leader {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String siren;
    private String role;
    private String lastName;
    private String firstName;
    private String gestionNumber;
    private String type;
    private String eventName;
    private String usageName;
    private String pseudo;

    @Column(length = 3000)
    private String companyName;
    private String legalForm;
    private String idData;
}
