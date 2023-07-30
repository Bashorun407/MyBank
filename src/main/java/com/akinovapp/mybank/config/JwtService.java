package com.akinovapp.mybank.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String STRING_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCq5i4HBECYGXAKbD3+4SQiYG3XQP1YhbeNTf" +
            "QmfliHFj6tgOoD0dv7ZdT3VcV0FgRagOfOR19iH9DOw4bpJ9mGivV61bmbAa72ITcGh306Ow1HEfgNZVwJLrMBcy4i+392XYlaz+V4Cs650" +
            "4j45fUlBPZtPGzxH8yWunm8pwDJNQIDAQAB";

    //1) Method to get SignIn key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(STRING_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //2) Method to extract all claims
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //3) Method to extract a claim...a generic method here
    //Generic method
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //4) Method to extract userName from token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //5) Method to generate token
    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails){

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 24)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //6) Generate token without extraClaims
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    //Method no (7) depends on Method (8) which also depends on Method (9)
    //7) To check if token is valid
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

    //8) To check that token is valid
    private boolean isTokenExpired(String token) {

        return  extractExpiration(token).before(new Date());
    }

    //9) To check token's expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
