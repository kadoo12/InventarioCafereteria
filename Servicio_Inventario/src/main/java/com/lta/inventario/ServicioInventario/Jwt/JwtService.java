package com.lta.inventario.ServicioInventario.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {

    private static final String SECRET_KEY="u8Fv9sXzQpLr3tYw5aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890==";

    public String getToken(UserDetails usuario) {
        System.out.println("GENERANDO TOKEN PARA USUARIO: "+usuario.getUsername());
    return getToken(new HashMap<>(), usuario);   
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails usuario){
        System.out.println("GENERANDO TOKEN CON CLAIMS EXTRA: "+extraClaims.toString());
        return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(usuario.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+3600000))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    private Key getKey() {
        System.out.println("OBTENIENDO CONTRASEÑA");
       byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(keyBytes);

    }

    public String extractUsername(String token) {
        System.out.println("EXTRAYENDO USUARIO DEL TOKEN");
        return getClaims(token,Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        System.out.println("VALIDANDO TOKEN PARA USUARIO: "+userDetails.getUsername());
        final String nomUsuario = extractUsername(token); 
        return (nomUsuario.equals(userDetails.getUsername())&& !isTokenExpired(token)) ;
    }

    private Claims getAllClaims(String token){
        System.out.println("OBTENIENDO CLAIMS DEL TOKEN");
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public <T> T getClaims(String token, Function<Claims,T> claimsResolver){
        System.out.println("OBTENIENDO CLAIMS ESPECIFICOS DEL TOKEN");
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
