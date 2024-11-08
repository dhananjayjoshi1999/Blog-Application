package com.springboot.blog.security;

import com.springboot.blog.exception.BlogApiException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    //Generate JWT token
    public String generateToken(Authentication authentication){

        String username= authentication.getName();

        Date currentDate = new Date();

        Date expirationDate= new Date(currentDate.getTime()+ 86400000);

        String token= Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(key())
                .compact();

        return token;


    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

    }

    // Get Username from JWT token

    public String getUsername(String token){

        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    // validate token

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
            return true;
        }catch (MalformedJwtException malformedJwtException){
            throw  new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid Jwt Token");
        }catch (ExpiredJwtException expiredJwtException){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        }catch (UnsupportedJwtException unsupportedJwtException){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        }catch (IllegalArgumentException illegalArgumentException){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Jwt claim key is null or empty");
        }

    }
}
