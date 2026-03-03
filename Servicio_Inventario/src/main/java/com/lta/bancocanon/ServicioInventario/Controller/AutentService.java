package com.lta.bancocanon.ServicioInventario.Controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lta.bancocanon.ServicioInventario.Jwt.JwtService;
import com.lta.bancocanon.ServicioInventario.Usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutentService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AutentResponse login(LoginRequest loginRequest) {
        System.out.println("ENTRANDO A LOGIN");
    authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(loginRequest.getNomUsuario(), 
                                            loginRequest.getContrasena()));
    System.out.println("USUARIO AUTENTICADO");

    UserDetails usuario = usuarioRepository.findByNomUsuario(loginRequest.getNomUsuario()).orElseThrow();
    System.out.println("USUARIO ENCONTRADO: "+usuario.getUsername());
    
    String token = jwtService.getToken(usuario);    
    return AutentResponse.builder()
            .token(token)
            .build();    
    
        }

}
