package br.com.universidade.apiboletos.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_STRING = "d2VzdGVybmNhbWVmdXJ0aGVyc2V0dGxld3Jvbmd1cHRlYW1yZXBsaWVkZHJpdmVmb3I=";

    private final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_STRING));

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractRa(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        Claims claims = extractClaims(token);
        Date exp = claims.getExpiration();
        return exp.after(new Date());
    }
}
