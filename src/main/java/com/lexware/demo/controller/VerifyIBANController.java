package com.lexware.demo.controller;

import com.lexware.demo.dto.IbanResponseDto;
import com.lexware.demo.services.VerifyIBANService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/iban")
public class VerifyIBANController {

    private final VerifyIBANService verifyIBANService;

    @Autowired
    public VerifyIBANController(final VerifyIBANService verifyIBANService) {
        this.verifyIBANService = verifyIBANService;
    }

    @GetMapping("/verifyIBAN")
    public IbanResponseDto verifyIBAN(@RequestParam String iban, @RequestParam boolean verifyExternal) {
        return verifyIBANService.verifyIBAN(iban, verifyExternal);
    }
}
