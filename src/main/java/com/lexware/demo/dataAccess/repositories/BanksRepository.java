package com.lexware.demo.dataAccess.repositories;

import com.lexware.demo.dataAccess.entities.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanksRepository extends CrudRepository<Bank, Long> {
    Bank findByBic(int bic);
}
