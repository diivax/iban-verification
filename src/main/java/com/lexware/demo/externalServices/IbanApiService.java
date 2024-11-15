package com.lexware.demo.externalServices;

import com.lexware.demo.externalServices.dtos.IbanApiResponseDto;
import com.lexware.demo.externalServices.errors.ExternalServiceError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class IbanApiService {

    private final WebClient webClient;

    public IbanApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.ibanapi.com/v1").build();
    }

    public boolean validateIban(String iban) {
        IbanApiResponseDto res = buildRequest(iban).block();

        return Optional.ofNullable(res).map(IbanApiResponseDto::getResult).orElse(0) == 200;
    }

    /**
     * Ignore deprecation warning as its the only way to control body extraction on error
     */
    private Mono<IbanApiResponseDto> buildRequest(final String iban) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/validate-basic/{iban}")
                        .queryParam("api_key", "504ac2648ee7c9dcca2f5bffc237437cfd2b0a6a")
                        .build(iban)).exchange().flatMap(clientRes -> {
                    if (clientRes.statusCode() == HttpStatus.BAD_REQUEST) {
                        return clientRes.bodyToMono(IbanApiResponseDto.class);
                    }

                    if (clientRes.statusCode().isError()) {
                        return Mono.error(new ExternalServiceError("Call to external service failed!"));
                    }

                    return clientRes.bodyToMono(IbanApiResponseDto.class);
                });
    }
}
