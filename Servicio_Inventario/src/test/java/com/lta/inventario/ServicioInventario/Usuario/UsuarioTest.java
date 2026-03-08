package com.lta.inventario.ServicioInventario.Usuario;

import com.lta.inventario.ServicioInventario.Jwt.*;
import com.lta.inventario.ServicioInventario.Controller.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

class UsuarioTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AutentService autentService;

    @BeforeEach
    void setUp() { 
        MockitoAnnotations.openMocks(this);
        autentService = new AutentService(usuarioRepository, jwtService, authenticationManager);
    }

    @Test
    void loginMustReturnToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setNomUsuario("Omar");
        loginRequest.setContrasena("1234");

        Usuario usuario = new Usuario();
        usuario.setNomUsuario("Omar");
        usuario.setContrasena("1234");

            String tokenEsperado = "token_de_prueba";
            
            when(usuarioRepository.findByNomUsuario("Omar")).thenReturn(Optional.of(usuario));
            when(jwtService.getToken(usuario)).thenReturn(tokenEsperado);

            AutentResponse response = autentService.login(loginRequest);

            assertEquals(tokenEsperado, response.getToken());
        

    }
}
