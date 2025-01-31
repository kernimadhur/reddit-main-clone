package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutheticationResponse {
        private String autheticationToken;
        private String username;
        private String refreshToken;
        private Instant expiresAt;

}
