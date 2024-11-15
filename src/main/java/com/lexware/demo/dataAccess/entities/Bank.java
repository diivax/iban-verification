package com.lexware.demo.dataAccess.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="BANKS")
@NoArgsConstructor
@Setter
@Getter
public class Bank {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    String name;
    int bic;

    public Bank(final String name, final int bic) {
        this.name = name;
        this.bic = bic;
    }
}
