package com.lta.inventario.ServicioInventario;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lta.inventario.ServicioInventario.Jwt.JwtService;
import com.lta.inventario.ServicioInventario.Usuario.*;

@SpringBootTest
public class ServicioInventarioTest {

    private JwtService jwtService;
    
    @Test
    void contextLoads() {

        assertNotNull(jwtService);

    }
@Test
    void testGeneracionDeToken() {
        Usuario usuario = new Usuario();
        usuario.setNomUsuario("omaarg");
        
        String token = jwtService.getToken(usuario);
        System.out.println(token);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }


}
