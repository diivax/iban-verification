package com.lexware.demo.dataAccess.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="COUNTRIES")
@NoArgsConstructor
@Setter
@Getter
public class Country {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    String name;
    String abbreviation;
    @Column(name="IBANLENGTH")
    int ibanLength;

    public Country(final String name, final String abbreviation, final int ibanLength) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.ibanLength = ibanLength;
    }
}