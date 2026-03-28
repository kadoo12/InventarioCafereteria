package com.lta.inventario.ServicioInventario.Inventario;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class InventarioService {

    private final InventarioRepository productoRepository;

    
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }
    
    @SuppressWarnings("null")
    public Producto agregaProducto(@NonNull ProductoRequest productorRequest) {

        return productoRepository.findByCodigo(productorRequest.getCodigo())
                .map((productoExiste) -> {
                    productoExiste.setCantidad(productoExiste.getCantidad() + productorRequest.getCantidad());

                    if(!productoExiste.getNombreProducto().equalsIgnoreCase(productorRequest.getNombreProducto())){
                        throw new RuntimeException("El codigo: " + productorRequest.getCodigo() + 
                        " ya pertenece al producto: " + productoExiste.getNombreProducto());
                    }
                    return productoRepository.save(productoExiste);
                })
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

    public Producto sumarAProducto(String codigoProducto, int cantidadASumar) {

        Producto producto = productoRepository.findByCodigo(codigoProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setCantidad(producto.getCantidad() + cantidadASumar);
        return productoRepository.save(producto);
    }

    public Producto descontarCantidad(String codigoProducto, int cantidadADescontar) {
        Producto producto = productoRepository.findByCodigo(codigoProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (cantidadADescontar < 0) {
            throw new RuntimeException("La cantidad a descontar no puede ser menor que cero");
        }
        if (producto.getCantidad() < cantidadADescontar) {
            throw new RuntimeException("Cantidad insuficiente en stock");
        }

        producto.setCantidad(producto.getCantidad() - cantidadADescontar);
        return productoRepository.save(producto);
    }

    public void eliminarProducto(String codigoProducto) {
        Producto producto = productoRepository.findByCodigo(codigoProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto != null) {
            productoRepository.delete(producto);
        }
    }
}
