package com.lexware.demo.dataAccess.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="IBAN_CACHE")
@NoArgsConstructor
@Setter
@Getter
public class Iban {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    String iban;
    boolean isValid;

    public Iban(final String iban, final boolean isValid) {
        this.iban = iban;
        this.isValid = isValid;
    }
}
