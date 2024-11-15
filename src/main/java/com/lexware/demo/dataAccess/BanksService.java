package com.lexware.demo.dataAccess;

import com.lexware.demo.dataAccess.repositories.BanksRepository;
import com.lexware.demo.dataAccess.entities.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BanksService {
    @Autowired
    private BanksRepository banksRepository;

    public Optional<Bank> getByBic(int bic) {
        return Optional.ofNullable(banksRepository.findByBic(bic));
    }
}
