package com.lta.inventario.ServicioInventario.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lta.inventario.ServicioInventario.Jwt.JwtAuthentificationFilter;

import org.springframework.security.authentication.AuthenticationProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthentificationFilter jwtAuthentificationFilter;
    private final AuthenticationProvider authenticationProvider; 

/*
 * IMPLEMENTACION DE CORS PARA EXPOSICION DEL BACKEND A PAGINA WEB
 */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        logger.debug("Configurando cadena de seguridad");
        
        return http
            .csrf(csrf -> csrf.disable())
            
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowedOrigins(java.util.List.of("http://localhost:3000", "http://localhost:8080", "http://localhost:8081", "http://127.0.0.1:3000"));
                corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfig.setAllowedHeaders(java.util.List.of("*"));
                corsConfig.setAllowCredentials(true);
                corsConfig.setExposedHeaders(java.util.List.of("Authorization"));
                corsConfig.setMaxAge(3600L);

                return corsConfig;
            }))
/*
 * CONFIGURACION DE ENDPOINTS DE ACCESO PERMITIDOS
 * permitAll: sin autenticacion
 * authenticated: es requerida una autenticacion para acceder al endpoint
 */
            .authorizeHttpRequests(authRequest -> authRequest   
                .requestMatchers("OPTIONS", "/**").permitAll()
                .requestMatchers("/controller/login").permitAll()
                .requestMatchers("/controller/**").authenticated()
                .anyRequest().authenticated()
                )  
            .sessionManagement(sessionManager
                -> sessionManager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
        
        }       
    
}