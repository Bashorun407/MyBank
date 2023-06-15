package com.akinovapp.mybank.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String STRING_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCq5i4HBECYGXAKbD3+4SQiYG3XQP1YhbeNTfQmfliHFj6tgOoD0dv7ZdT3VcV0FgRagOfOR19iH9DOw4bpJ9mGivV61bmbAa72ITcGh306Ow1HEfgNZVwJLrMBcy4i+392XYlaz+V4Cs6504j45fUlBPZtPGzxH8yWunm8pwDJNQIDAQAB";
    public String extractUserName(String token) {
        return null;
    }

    //Generic method
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(STRING_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
