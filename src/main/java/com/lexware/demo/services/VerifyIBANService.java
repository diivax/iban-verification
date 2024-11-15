package com.lexware.demo.services;

import com.lexware.demo.externalServices.IbanApiService;
import com.lexware.demo.dataAccess.BanksService;
import com.lexware.demo.dataAccess.CountryService;
import com.lexware.demo.dataAccess.IbanService;
import com.lexware.demo.dto.IbanResponseDto;
import com.lexware.demo.dataAccess.entities.Bank;
import com.lexware.demo.dataAccess.entities.Country;
import com.lexware.demo.dataAccess.entities.Iban;
import com.lexware.demo.externalServices.dtos.IbanApiResponseDto;
import com.lexware.demo.services.errors.VerifyIBANServiceError;
import nl.garvelink.iban.Modulo97;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VerifyIBANService {
    @Autowired
    private final IbanService ibanService;
    @Autowired
    private final BanksService banksService;
    @Autowired
    private final CountryService countryService;
    @Autowired
    private final IbanApiService ibanApi;

    Logger log = LoggerFactory.getLogger(VerifyIBANService.class);

    public VerifyIBANService(IbanService ibanService, BanksService banksService, CountryService countryService, IbanApiService ibanApi) {
        this.ibanApi = ibanApi;
        this.ibanService = ibanService;
        this.banksService = banksService;
        this.countryService = countryService;
    }

    public IbanResponseDto verifyIBAN(String iban, boolean verifyExternal) {
        Optional.ofNullable(iban).filter(pred -> pred.length() >= 15)
                .orElseThrow(() -> new VerifyIBANServiceError("IBAN must be at least 15 characters long!"));
        checkForSpecialCharacters(iban);
        Optional<Iban> maybeCached = retrieveIfCached(iban);

        if (maybeCached.isPresent()) {
            final String cachedIban = maybeCached.map(Iban::getIban)
                    .orElseThrow(() -> new VerifyIBANServiceError("Error while retrieving cached IBAN!"));

            return new IbanResponseDto(cachedIban,
                    maybeCached.map(Iban::isValid)
                            .orElseThrow(() -> new VerifyIBANServiceError("Error while retrieving cached IBAN!")),
                    getBankName(cachedIban));
        }

        boolean isValid = false;

        if (verifyExternal) {
            isValid = verifyIbanExternally(iban);
        } else {
            isValid = validateIban(iban);
        }

        String bankName = "";

        if (isValid) {
            bankName = getBankName(iban);
        }

        cacheIban(iban, isValid);

        return new IbanResponseDto(iban, isValid, bankName);
    }

    private boolean verifyIbanExternally(String iban) {
        return ibanApi.validateIban(iban);
    }

    private String getBankName(String iban) {
        String bicString = iban.substring(4, 12);

        try {
            int bic = Integer.parseInt(bicString);

            Optional<Bank> bank = banksService.getByBic(bic);
            return bank.map(Bank::getName).orElse("");
        } catch (Exception e) {
            log.info("bic {} could not be parsed", iban);
        }

        return "";
    }

    private boolean validateIban(final String iban) {
        return validateIbanChecksum(iban) && validateIbanLength(iban);
    }

    private boolean validateIbanChecksum(final String iban) {
        try {
            return Modulo97.verifyCheckDigits(iban);
        } catch (Exception e) {
            throw new VerifyIBANServiceError("Iban could not be validated", e, null);
        }
    }

    private boolean validateIbanLength(final String iban) {
        Optional<Country> maybeCountry = countryService.getByAbbreviation(iban.substring(0, 2));
        return maybeCountry.map(Country::getIbanLength).map(length -> length == iban.length()).orElse(false);
    }

    private void checkForSpecialCharacters(String iban) {
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        Matcher hasSpecial = special.matcher(iban);

        if (hasSpecial.find()) {
            throw new VerifyIBANServiceError("No special characters allowed!");
        }
    }

    private Optional<Iban> retrieveIfCached(String iban) {
        return ibanService.getByIban(iban);
    }

    private void cacheIban(final String iban, final boolean isValid) {
        ibanService.saveIban(iban, isValid);
    }
}
