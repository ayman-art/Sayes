package com.utopia.Sayes.Modules;

import com.utopia.Sayes.enums.Role;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class Authentication {
    static Dotenv dotenv = Dotenv.configure().load();


    public static String generateJWT(long id, String username, Role role){
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        String envKey = dotenv.get("JWT_Key");
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(envKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setId(String.valueOf(id))
                .setIssuedAt(today)
                .setSubject(username)
                .claim("role", role)
                .signWith(algorithm, signingKey)
                .setExpiration(new Date(now + 86400000)); // a day
        return builder.compact();
    }


    public static Claims parseToken(String jwt){

        String envKey = dotenv.get("JWT_Key");
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(envKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
