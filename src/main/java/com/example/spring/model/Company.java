package com.example.spring.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
@Entity
@Table(name = "Companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denomination;
    private String siren;
    private String nic;
    private String formeJuridique;
    @Column(name = "code_ape")
    private String codeAPE;
    private String adresse;
    private String codePostal;
    private String ville;
    private String region;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateImmatriculation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateRadiation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_1_2018;
    private Double CA_1_2018;
    private Double resultat_1_2018;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_2_2018;
    private Double CA_2_2018;
    private Double resultat_2_2018;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_3_2018;
    private Double CA_3_2018;
    private Double resultat_3_2018;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_1_2019;
    private Double CA_1_2019;
    private Double resultat_1_2019;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_2_2019;
    private Double CA_2_2019;
    private Double resultat_2_2019;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_3_2019;
    private Double CA_3_2019;
    private Double resultat_3_2019;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_1_2020;
    private Double CA_1_2020;
    private Double resultat_1_2020;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_2_2020;
    private Double CA_2_2020;
    private Double resultat_2_2020;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_3_2020;
    private Double CA_3_2020;
    private Double resultat_3_2020;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_1_2021;
    private Double CA_1_2021;
    private Double resultat_1_2021;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_2_2021;
    private Double CA_2_2021;
    private Double resultat_2_2021;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_3_2021;
    private Double CA_3_2021;
    private Double resultat_3_2021;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_1_2022;
    private Double CA_1_2022;
    private Double resultat_1_2022;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_2_2022;
    private Double CA_2_2022;
    private Double resultat_2_2022;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_3_2022;
    private Double CA_3_2022;
    private Double resultat_3_2022;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_1_2023;
    private Double CA_1_2023;
    private Double resultat_1_2023;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_2_2023;
    private Double CA_2_2023;
    private Double resultat_2_2023;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_3_2023;
    private Double CA_3_2023;
    private Double resultat_3_2023;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_1;
    private Double CA_1;
    private Double resultat_1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_2;
    private Double CA_2;
    private Double resultat_2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice_3;
    private Double CA_3;
    private Double resultat_3;

    private String secteurActivite;
    private String phone;
    @Column(length = 3000)
    private String website;
    @Column(length = 10000)
    private String reviews;
    @Column(length = 10000)
    private String schedule;
    @Column(length = 3000)
    private String instagram;
    @Column(length = 3000)
    private String facebook;
    @Column(length = 3000)
    private String twitter;
    @Column(length = 3000)
    private String linkedin;
    @Column(length = 3000)
    private String youtube;
    @Column(length = 3000)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfScrapping;
}
