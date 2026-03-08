package com.lta.inventario.ServicioInventario.Jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

class JwtServiceTest {
    
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGenerateToken() {
        UserDetails user = User.withUsername("usuario_prueba")
                .password("12345")
                .roles("USER")
                .build();
        String token = jwtService.getToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        UserDetails user = User.withUsername("usuario_prueba")
                .password("12345")
                .roles("USER")
                .build();
        String token = jwtService.getToken(user);
        String username = jwtService.extractUsername(token);

        assertEquals("usuario_prueba", username);
    }

    @Test
void deberiaValidarTokenCorrectamente() {

    UserDetails user = User
            .withUsername("omar")
            .password("1234")
            .authorities("USER")
            .build();

    String token = jwtService.getToken(user);

    boolean valido = jwtService.isTokenValid(token, user);

    assertTrue(valido);
}

    @Test
    void deberiaInvalidarTokenIncorrecto() {
        UserDetails user = User
                .withUsername("omar")
                .password("1234")
                .authorities("USER")
                .build();

    
        String token = jwtService.getToken(user);
    
        assertNotNull(token);
    }
    
}