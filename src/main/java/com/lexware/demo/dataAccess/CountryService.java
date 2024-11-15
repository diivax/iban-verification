package com.lexware.demo.dataAccess;

import com.lexware.demo.dataAccess.repositories.CountryRepository;
import com.lexware.demo.dataAccess.entities.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public Optional<Country> getByAbbreviation(String abbreviation) {
        return Optional.ofNullable(countryRepository.findByAbbreviation(abbreviation));
    }
}
