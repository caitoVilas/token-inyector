package com.caito.tokeninyector.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.caito.tokeninyector.api.exceptions.customs.TokenExpirationException;
import com.caito.tokeninyector.components.KeyUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final KeyUtils keyUtils;
    private final ScheduledExecutorService scheduledExecutorService;
    @Value("${jwt.expiration}")
    private Integer expiration;
    private final String TIMEOUT_MSG = "--> cancelling task due to timeout";

    @Async("taskExecutor")
    public CompletableFuture<String> generateToken(String subject){
        try {
            Algorithm algorithm = Algorithm.RSA256(null, keyUtils.getPrivateKey());
            var response = CompletableFuture.completedFuture(JWT.create()
                    .withSubject(subject)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                    .sign(algorithm));
            scheduledExecutorService.schedule(()-> {
                if (!response.isDone()){
                    log.warn(TIMEOUT_MSG);
                    response.cancel(true);
                }
            }, 180, TimeUnit.SECONDS);
            return response;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<DecodedJWT> validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.RSA256(keyUtils.getPublicKey(), null);
            JWTVerifier verifier = JWT.require(algorithm).build();
            var response = CompletableFuture.completedFuture(verifier.verify(token));
            scheduledExecutorService.schedule(()-> {
                if (!response.isDone()){
                    log.warn(TIMEOUT_MSG);
                    response.cancel(true);
                }
            }, 180, TimeUnit.SECONDS);
            return response;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (JWTVerificationException e) {
            log.error("--> ERROR: el token ha expirado");
            throw new TokenExpirationException("token expirado");
        }catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}
