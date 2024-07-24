package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

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
    private Long gestionNumber;
    private String type;
    private String eventName;
    private String greffe;

    @Column(name = "date_of_greffe")
    private Date dateOfGreffe;
    private String usageName;
    private String pseudo;
    private String companyName;
    private String legalForm;
    private long idData;
}
