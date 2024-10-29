package com.idealbank.accounts.service.client;

import com.idealbank.accounts.dto.CardsDto;
import com.idealbank.accounts.service.impl.CardsFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", fallback = CardsFallback.class)
public interface CardsFeignClient {

    @GetMapping("/api/fetch")
    ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader(name = "idealbank-correlation-id") String correlationId, @RequestParam String mobileNumber);
}
