
package com.lta.inventario.ServicioInventario.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.lta.inventario.ServicioInventario.Usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AplicacionConfig {

    private final UsuarioRepository usuarioRepository;

    @Bean
    public AuthenticationManager authentificationManager(AuthenticationConfiguration config)throws Exception{
        System.out.println("ENTRANDO A AUTHENTICATION MANAGER");
        return config.getAuthenticationManager();
    }

    @Bean    
    public AuthenticationProvider authenticationProvider(){
        System.out.println("ENTRANDO A AUTHENTICATION PROVIDER");

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(usuarioDetailService());
        authenticationProvider.setPasswordEncoder(contraseñaEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder contraseñaEncoder() {
        System.out.println("ENTRANDO A CONTRASEÑA ENCODER");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarioDetailService() {
        System.out.println("ENTRANDO A USUARIO DETAIL SERVICE");
        return nomUsuario -> usuarioRepository.findByNomUsuario(nomUsuario)
            .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
    }

}
