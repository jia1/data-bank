/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokenManagement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Clarissa Poedjiono
 */
public class tokenManagement {

    /**
     * @param args the command line arguments
     */
    //to test the methods
    public static void main(String[] args) {
        String token = (createJWT("huiyan", "clarissa", "token"));   
        System.out.println(token);
        System.out.println(parseJWT(token));
        
    }
    
    //JWT generation
    public static String createJWT(String id, String issuer, String subject) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("z4z4Lab#?DUQVP4&");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                                    .setIssuedAt(now)
                                    .setSubject(subject)
                                    .setIssuer(issuer)
                                    .signWith(signatureAlgorithm, signingKey);

        //comment in to add token expiration 
//        if (ttlMillis >= 0) {
//        long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
    
    //JWT validation
    public static String parseJWT(String jwt) {
 
    //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()         
           .setSigningKey(DatatypeConverter.parseBase64Binary("z4z4Lab#?DUQVP4&"))
           .parseClaimsJws(jwt).getBody();
        return claims.getId();
      /*System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration()); */
    }
    
}
