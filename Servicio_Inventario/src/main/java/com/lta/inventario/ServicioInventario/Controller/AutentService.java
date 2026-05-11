package com.lta.inventario.ServicioInventario.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lta.inventario.ServicioInventario.Jwt.JwtService;
import com.lta.inventario.ServicioInventario.Usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutentService {

    private static final Logger logger = LoggerFactory.getLogger(AutentService.class);

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AutentResponse login(LoginRequest loginRequest) {
        logger.info("Intento de login para usuario: {}", loginRequest.getNomUsuario());
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getNomUsuario(), 
                    loginRequest.getContrasena()
                )
            );
            logger.info("Autenticación exitosa para: {}", loginRequest.getNomUsuario());
        } catch (Exception e) {
            logger.warn("Fallo en autenticación para usuario: {}", loginRequest.getNomUsuario());
            throw e;
        }

        UserDetails usuario = usuarioRepository.findByNomUsuario(loginRequest.getNomUsuario()).orElseThrow();
        
        String token = jwtService.getToken(usuario);
        logger.info("Token generado para usuario: {}", usuario.getUsername());
        
        return AutentResponse.builder()
                .token(token)
                .build();    
    }

}