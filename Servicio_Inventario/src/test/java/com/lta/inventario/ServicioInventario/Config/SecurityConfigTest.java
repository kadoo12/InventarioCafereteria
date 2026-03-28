package com.lta.inventario.ServicioInventario.Config;

//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

@Autowired    
private MockMvc mockMvc;


    @Test
    void deberiaConfigurarSeguridad() throws Exception {
        String requestBody = """
    {
    "nomUsuario": "omaarg",
    "contrasena": "contrasena123"    
    }
                """;
                mockMvc.perform(post("/controller/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk());
    } 
}
