package com.javarush.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "city", schema = "world")
public class CityEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    private String district;

    private Integer population;
}
