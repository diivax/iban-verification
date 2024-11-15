package com.lexware.demo.dataAccess.repositories;

import com.lexware.demo.dataAccess.entities.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    Country findByAbbreviation(String abbreviation);
}
