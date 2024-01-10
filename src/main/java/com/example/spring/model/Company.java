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
    private LocalDate dateClotureExercice1_2019;
    private Long CA1_2019;
    private Long resultat1_2019;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice2_2019;
    private Long CA2_2019;
    private Long resultat2_2019;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice3_2019;
    private Long CA3_2019;
    private Long resultat3_2019;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice1_2020;
    private Long CA1_2020;
    private Long resultat1_2020;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice2_2020;
    private Long CA2_2020;
    private Long resultat2_2020;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice3_2020;
    private Long CA3_2020;
    private Long resultat3_2020;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice1_2021;
    private Long CA1_2021;
    private Long resultat1_2021;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice2_2021;
    private Long CA2_2021;
    private Long resultat2_2021;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice3_2021;
    private Long CA3_2021;
    private Long resultat3_2021;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice1_2022;
    private Long CA1_2022;
    private Long resultat1_2022;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice2_2022;
    private Long CA2_2022;
    private Long resultat2_2022;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice3_2022;
    private Long CA3_2022;
    private Long resultat3_2022;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice1_2023;
    private Long CA1_2023;
    private Long resultat1_2023;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice2_2023;
    private Long CA2_2023;
    private Long resultat2_2023;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice3_2023;
    private Long CA3_2023;
    private Long resultat3_2023;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice1;
    private Long CA1;
    private Long resultat1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice2;
    private Long CA2;
    private Long resultat2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClotureExercice3;
    private Long CA3;
    private Long resultat3;

    private String secteurActivite;
    private String phone;
    private String website;
    private String reviews;
    private String schedule;
    private String instagram;
    private String facebook;
    private String twitter;
    private String linkedin;
    private String youtube;
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfScrapping;
}
