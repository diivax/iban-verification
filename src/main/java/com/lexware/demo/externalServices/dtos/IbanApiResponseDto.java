package com.lexware.demo.externalServices.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IbanApiResponseDto {
    Integer result;
    String message;
    Collection<Validations> validations;
    Integer expremental;
    Data data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Validations {
        Integer result;
        String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Data {
        @JsonProperty("country_code")
        String countryCode;
        @JsonProperty("iso_alpha3")
        String iso;
        @JsonProperty("country_name")
        String name;
        @JsonProperty("currency_code")
        String currencyCode;
        @JsonProperty("sepa_member")
        String sepaMember;
        String bban;
        @JsonProperty("bank_account")
        String bankAccount;
    }
}