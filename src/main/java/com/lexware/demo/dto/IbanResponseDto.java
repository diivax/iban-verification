package com.lexware.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class IbanResponseDto {

    String iban;
    boolean isValid;
    String bankName;
}
