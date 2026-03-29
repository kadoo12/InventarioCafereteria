package com.lta.inventario.ServicioInventario.Jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthentificationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthentificationFilter filter;

    @BeforeEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    //NO HAY TOKEN
    @SuppressWarnings("null")
    @Test
    void deberiaContinuarFiltroSiNoHayToken() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    //HEADER INVALIDO
    @SuppressWarnings("null")
    @Test
    void deberiaContinuarSiHeaderNoEsBearer() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic 123");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    //TOKEN PERO USERNAME NULL
    @SuppressWarnings("null")
    @Test
    void noDebeAutenticarSiUsernameEsNull() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token123");
        when(jwtService.extractUsername("token123")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    //TOKEN INVALIDO
    @SuppressWarnings("null")
    @Test
    void noDebeAutenticarSiTokenEsInvalido() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token123");
        when(jwtService.extractUsername("token123")).thenReturn("omar");

        UserDetails userDetails = User.withUsername("omar").password("123").authorities("USER").build();

        when(userDetailsService.loadUserByUsername("omar")).thenReturn(userDetails);
        when(jwtService.isTokenValid("token123", userDetails)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    // TOKEN VALIDO => AUTENTICACION EXITOSA
    @SuppressWarnings("null")
    @Test
    void deberiaAutenticarUsuarioSiTokenEsValido() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token123");
        when(jwtService.extractUsername("token123")).thenReturn("omar");

        UserDetails userDetails = User.withUsername("omar").password("123").authorities("USER").build();

        when(userDetailsService.loadUserByUsername("omar")).thenReturn(userDetails);
        when(jwtService.isTokenValid("token123", userDetails)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        //VERIFICA QUE SE AUTENTICÓ CORRECTAMENTE
        assert(SecurityContextHolder.getContext().getAuthentication() != null);
    }
}