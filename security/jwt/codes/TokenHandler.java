package com.npw.api.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.xml.bind.DatatypeConverter;

public class TokenHandler {

    private static final byte[] SECRET=DatatypeConverter.parseBase64Binary("secret");

    public TokenHandler(){}
    
    public String getSubjectFromToken(String token) {
        return  Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String createTokenFromSubject(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

}
