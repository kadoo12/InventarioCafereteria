package com.lta.inventario.ServicioInventario.Inventario;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventarioService {

    private final InventarioRepository productoRepository;

    
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }
    
    public Producto agregaProducto(ProductoRequest productorRequest) {

        return productoRepository.findByCodigo(productorRequest.getCodigo())
                .orElseGet(() -> {
                    Producto nuevoProducto = Producto.builder()
                            .codigo(productorRequest.getCodigo())
                            .nombreProducto(productorRequest.getNombreProducto())
                            .precio(productorRequest.getPrecio())
                            .cantidad(productorRequest.getCantidad())
                            .build();
                    return productoRepository.save(nuevoProducto);
                }); 
    }

    public Producto sumarAProducto(String nombreProducto, int cantidadASumar) {

        Producto producto = productoRepository.findByNombreProducto(nombreProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setCantidad(producto.getCantidad() + cantidadASumar);
        return productoRepository.save(producto);
    }
    
}
