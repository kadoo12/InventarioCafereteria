/*
 * Clase que realiza el filtro de autentifiacion
 * 
 * Primero se realiza la verificacion de existencia del token, si es nulo no devuelve nada
 * 
*/

package com.lta.inventario.ServicioInventario.Jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthentificationFilter.class);

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;


    @Bean
public FilterRegistrationBean<JwtAuthentificationFilter> jwtFilterRegistration(JwtAuthentificationFilter filter) {
    FilterRegistrationBean<JwtAuthentificationFilter> registration = new FilterRegistrationBean<>(filter);
    registration.setEnabled(false); // Evita que se ejecute dos veces
    return registration;
}

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        final String token = getTokenFromRequest(request);
        final String nomUsuario;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            nomUsuario = jwtService.extractUsername(token);
            
            if (nomUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(nomUsuario);
                
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("Usuario {} autenticado por JWT", nomUsuario);
                }
            }
        } catch (Exception e) {
            logger.debug("Error al validar JWT: {}", e.getMessage());
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