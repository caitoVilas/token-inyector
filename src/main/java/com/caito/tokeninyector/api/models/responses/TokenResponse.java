package com.caito.tokeninyector.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class TokenResponse implements Serializable {
    private String token;
}
