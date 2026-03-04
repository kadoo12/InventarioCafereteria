/*
 * Clase que realiza el filtro de autentifiacion
 * 
 * Primero se realiza la verificacion de existencia del token, si es nulo no devuelve nada
 * 
*/

package com.lta.inventario.ServicioInventario.Jwt;

import java.io.IOException;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;


    @Bean
public FilterRegistrationBean<JwtAuthentificationFilter> jwtFilterRegistration(JwtAuthentificationFilter filter) {
    FilterRegistrationBean<JwtAuthentificationFilter> registration = new FilterRegistrationBean<>(filter);
    registration.setEnabled(false); // Evita que se ejecute dos veces
    return registration;
}

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
System.out.println("ENTRANDO A JWT AUTHENTICATION FILTER");

// Ejecuta esto en un Main o un Test para obtener el valor
//System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("contrasena123"));

        final String token = getTokenFromRequest(request);
        final String nomUsuario;

        if (token == null) {
            filterChain.doFilter(request, response);
            System.out.println("NO SE ENCONTRÓ TOKEN, CONTINUANDO CON EL FILTRO");
            return;
        }
        System.out.println("TOKEN ENCONTRADO: "+token);
        nomUsuario = jwtService.extractUsername(token);
System.out.println("USUARIO EXTRAIDO DEL TOKEN: "+nomUsuario);
        if (nomUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(nomUsuario);
System.out.println("USUARIO DETALLADO: "+userDetails.getUsername());
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null, 
                    userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                System.out.println("USUARIO AUTENTICADO, CONFIGURANDO SECURITY CONTEXT");
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }


    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}