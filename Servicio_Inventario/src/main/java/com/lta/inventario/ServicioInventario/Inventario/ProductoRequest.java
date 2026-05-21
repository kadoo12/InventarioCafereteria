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
    
    public String codigo;
    public String nombreProducto;
    public int precio;
    public int cantidad;
    public Integer categoriaId;
}
