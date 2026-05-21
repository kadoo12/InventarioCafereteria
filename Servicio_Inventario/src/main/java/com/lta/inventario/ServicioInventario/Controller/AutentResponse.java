package com.lta.inventario.ServicioInventario.Controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutentResponse {

    String token;
    String rol;
    String nomUsuario;
}
