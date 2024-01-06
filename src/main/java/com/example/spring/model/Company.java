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

    public Company(String denomination, String siren, String nic, String formeJuridique, String codeAPE, String adresse, String codePostal, String ville, String region, LocalDate dateImmatriculation, LocalDate dateRadiation,
                   LocalDate dateClotureExercice1_2019, Long CA1_2019, Long resultat1_2019, LocalDate dateClotureExercice2_2019, Long CA2_2019, Long resultat2_2019, LocalDate dateClotureExercice3_2019, Long CA3_2019, Long resultat3_2019,
                   LocalDate dateClotureExercice1_2020, Long CA1_2020, Long resultat1_2020, LocalDate dateClotureExercice2_2020, Long CA2_2020, Long resultat2_2020, LocalDate dateClotureExercice3_2020, Long CA3_2020, Long resultat3_2020,
                   LocalDate dateClotureExercice1_2021, Long CA1_2021, Long resultat1_2021, LocalDate dateClotureExercice2_2021, Long CA2_2021, Long resultat2_2021, LocalDate dateClotureExercice3_2021, Long CA3_2021, Long resultat3_2021,
                   LocalDate dateClotureExercice1_2022, Long CA1_2022, Long resultat1_2022, LocalDate dateClotureExercice2_2022, Long CA2_2022, Long resultat2_2022, LocalDate dateClotureExercice3_2022, Long CA3_2022, Long resultat3_2022,
                   LocalDate dateClotureExercice1_2023, Long CA1_2023, Long resultat1_2023, LocalDate dateClotureExercice2_2023, Long CA2_2023, Long resultat2_2023, LocalDate dateClotureExercice3_2023, Long CA3_2023, Long resultat3_2023,
                   LocalDate dateClotureExercice1, Long CA1, Long resultat1, LocalDate dateClotureExercice2, Long CA2, Long resultat2, LocalDate dateClotureExercice3, Long CA3, Long resultat3,
                   String secteurActivite, String phone, String website, String reviews, String schedule, String instagram, String facebook, String twitter, String linkedin, String youtube, String email, LocalDate dateOfScrapping) {

        this.denomination = denomination;
        this.siren = siren;
        this.nic = nic;
        this.formeJuridique = formeJuridique;
        this.codeAPE = codeAPE;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.region = region;
        this.dateImmatriculation = dateImmatriculation;
        this.dateRadiation = dateRadiation;
        this.dateClotureExercice1_2019 = dateClotureExercice1_2019;
        this.CA1_2019 = CA1_2019;
        this.resultat1_2019 = resultat1_2019;
        this.dateClotureExercice2_2019 = dateClotureExercice2_2019;
        this.CA2_2019 = CA2_2019;
        this.resultat2_2019 = resultat2_2019;
        this.dateClotureExercice3_2019 = dateClotureExercice3_2019;
        this.CA3_2019 = CA3_2019;
        this.resultat3_2019 = resultat3_2019;
        this.dateClotureExercice1_2020 = dateClotureExercice1_2020;
        this.CA1_2020 = CA1_2020;
        this.resultat1_2020 = resultat1_2020;
        this.dateClotureExercice2_2020 = dateClotureExercice2_2020;
        this.CA2_2020 = CA2_2020;
        this.resultat2_2020 = resultat2_2020;
        this.dateClotureExercice3_2020 = dateClotureExercice3_2020;
        this.CA3_2020 = CA3_2020;
        this.resultat3_2020 = resultat3_2020;
        this.dateClotureExercice1_2021 = dateClotureExercice1_2021;
        this.CA1_2021 = CA1_2021;
        this.resultat1_2021 = resultat1_2021;
        this.dateClotureExercice2_2021 = dateClotureExercice2_2021;
        this.CA2_2021 = CA2_2021;
        this.resultat2_2021 = resultat2_2021;
        this.dateClotureExercice3_2021 = dateClotureExercice3_2021;
        this.CA3_2021 = CA3_2021;
        this.resultat3_2021 = resultat3_2021;
        this.dateClotureExercice1_2022 = dateClotureExercice1_2022;
        this.CA1_2022 = CA1_2022;
        this.resultat1_2022 = resultat1_2022;
        this.dateClotureExercice2_2022 = dateClotureExercice2_2022;
        this.CA2_2022 = CA2_2022;
        this.resultat2_2022 = resultat2_2022;
        this.dateClotureExercice3_2022 = dateClotureExercice3_2022;
        this.CA3_2022 = CA3_2022;
        this.resultat3_2022 = resultat3_2022;
        this.dateClotureExercice1_2023 = dateClotureExercice1_2023;
        this.CA1_2023 = CA1_2023;
        this.resultat1_2023 = resultat1_2023;
        this.dateClotureExercice2_2023 = dateClotureExercice2_2023;
        this.CA2_2023 = CA2_2023;
        this.resultat2_2023 = resultat2_2023;
        this.dateClotureExercice3_2023 = dateClotureExercice3_2023;
        this.CA3_2023 = CA3_2023;
        this.resultat3_2023 = resultat3_2023;
        this.dateClotureExercice1 = dateClotureExercice1_2021;
        this.CA1 = CA1_2021;
        this.resultat1 = resultat1_2021;
        this.dateClotureExercice2 = dateClotureExercice2_2021;
        this.CA2 = CA2_2021;
        this.resultat2 = resultat2_2021;
        this.dateClotureExercice3 = dateClotureExercice3_2021;
        this.CA3 = CA3_2021;
        this.resultat3 = resultat3_2021;
        this.secteurActivite = secteurActivite;
        this.phone = phone;
        this.website = website;
        this.reviews = reviews;
        this.schedule = schedule;
        this.instagram = instagram;
        this.facebook = facebook;
        this.twitter = twitter;
        this.linkedin = linkedin;
        this.youtube = youtube;
        this.email = email;
        this.dateOfScrapping = dateOfScrapping;
    }
}
