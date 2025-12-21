package com.example.spring.app.leader;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leaders")
public class LeaderModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
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
