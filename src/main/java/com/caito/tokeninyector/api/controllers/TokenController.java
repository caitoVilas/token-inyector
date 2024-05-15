package com.caito.tokeninyector.api.controllers;

import com.caito.tokeninyector.api.models.request.TechnicianRequest;
import com.caito.tokeninyector.api.models.responses.TokenResponse;
import com.caito.tokeninyector.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v2/inyector")
@Slf4j
@RequiredArgsConstructor
public class TokenController {
    private final JwtService jwtService;

    @PostMapping("/generate-token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TechnicianRequest request) {
        log.info("--> endpoint POST Creating token");
        var token = jwtService.generateToken(request.getName()).join();
        return ResponseEntity.ok(TokenResponse.builder().token(token).build());
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        log.info("--> endpoint POST Validating token");
        var response = jwtService.validateToken(token).join();
        return ResponseEntity.ok(response);
    }

}
