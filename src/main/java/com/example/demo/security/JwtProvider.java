package com.example.demo.security;

import com.example.demo.exception.RedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private  KeyStore keyStore;

    @PostConstruct                      //Spring calls methods annotated with @PostConstruct only once, just after the initialization of bean properties.
    public void init(){
        try{
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceStream,"secret".toCharArray());
        }
        catch (KeyStoreException| CertificateException | NoSuchAlgorithmException | IOException e){

        }
    }

    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername())
                .signWith(getPrivateKey()).compact();
    }

    private Key getPrivateKey() {
        try {
            return keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RedditException("Exception occurred while retrieving public key from keystore ");
        }
    }

    public boolean validateToken(String jwt){
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;

    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new RedditException("Exception occurred while retrieving public key");
        }

    }

    public String getUserNameFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();
        return claims.getSubject();
     }

}

