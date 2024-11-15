package com.lexware.demo.dataAccess;

import com.lexware.demo.dataAccess.repositories.IbanRepository;
import com.lexware.demo.dataAccess.entities.Iban;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IbanService {
    @Autowired
    private IbanRepository ibanRepository;

    public Optional<Iban> getByIban(String iban) {
        return Optional.ofNullable(ibanRepository.findByIban(iban));
    }

    public void saveIban(String iban, boolean isValid) {
        ibanRepository.save(new Iban(iban, isValid));
    }
}
