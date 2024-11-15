package com.lexware.demo.dataAccess.repositories;

import com.lexware.demo.dataAccess.entities.Iban;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IbanRepository extends CrudRepository<Iban, Long> {
    Iban findByIban(String iban);
}
