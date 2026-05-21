package com.lta.inventario.ServicioInventario.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lta.inventario.ServicioInventario.Usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final String SECRET_KEY="u8Fv9sXzQpLr3tYw5aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890==";

    public String getToken(UserDetails usuario) {
        logger.debug("Generando JWT para usuario: {}", usuario.getUsername());
        Map<String, Object> extraClaims = new HashMap<>();
        
        if (usuario instanceof Usuario) {
            Usuario usuarioEntity = (Usuario) usuario;
            extraClaims.put("rol", usuarioEntity.getRol());
        }
        
        return getToken(extraClaims, usuario);   
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails usuario){
        String token = Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(usuario.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+3600000))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
        
        logger.debug("JWT generado exitosamente");
        return token;
    }

    private Key getKey() {
       byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return getClaims(token,Claims::getSubject);
    }

    public String extractRol(String token) {
        return getClaims(token, claims -> claims.get("rol", String.class));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String nomUsuario = extractUsername(token); 
        return (nomUsuario.equals(userDetails.getUsername())&& !isTokenExpired(token)) ;
    }

    private Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public <T> T getClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token){
        return getClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
}
