package com.lta.inventario.ServicioInventario.Inventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {
    
String codigo;
String nombreProducto;
int precio;
int cantidad;

}
