package com.lta.inventario.ServicioInventario.Inventario;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lta.inventario.ServicioInventario.Categoria.Categoria;
import com.lta.inventario.ServicioInventario.Categoria.CategoriaRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);
    private final InventarioRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    
    public List<Producto> obtenerProductos() {
        logger.debug("Obteniendo todos los productos");
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosPorCategoria(Integer categoriaId) {
        logger.debug("Obteniendo productos de la categoría: {}", categoriaId);
        return productoRepository.findByCategoriaId(categoriaId);
    }
    
    @SuppressWarnings("null")
    public Producto agregaProducto(@NonNull ProductoRequest request) {
        logger.info("Agregando producto: {} con categoriaId: {}", request.codigo, request.categoriaId);

        return productoRepository.findByCodigo(request.codigo)
                .map((productoExiste) -> {
                    productoExiste.setCantidad(productoExiste.getCantidad() + request.cantidad);

                    if(!productoExiste.getNombreProducto().equalsIgnoreCase(request.nombreProducto)){
                        throw new RuntimeException("El codigo: " + request.codigo + 
                        " ya pertenece al producto: " + productoExiste.getNombreProducto());
                    }
                    return productoRepository.save(productoExiste);
                })
                .orElseGet(() -> {
                    Categoria categoria = null;
                    
                    // Si se proporciona categoriaId, buscarla
                    if (request.categoriaId != null && request.categoriaId > 0) {
                        logger.info("Buscando categoría con ID: {}", request.categoriaId);
                        categoria = categoriaRepository.findById(request.categoriaId).orElse(null);
                        if (categoria != null) {
                            logger.info("Categoría encontrada: {}", categoria.getNombre());
                        } else {
                            logger.warn("Categoría no encontrada para ID: {}", request.categoriaId);
                        }
                    }
                    
                    Producto nuevoProducto = Producto.builder()
                            .codigo(request.codigo)
                            .nombreProducto(request.nombreProducto)
                            .precio(request.precio)
                            .cantidad(request.cantidad)
                            .categoria(categoria)
                            .build();
                    
                    logger.info("Guardando nuevo producto: {}", request.codigo);
                    return productoRepository.save(nuevoProducto);
                }); 
    }

    public Producto sumarAProducto(String codigoProducto, int cantidadASumar) {
        logger.info("Sumando {} unidades al producto: {}", cantidadASumar, codigoProducto);

        Producto producto = productoRepository.findByCodigo(codigoProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setCantidad(producto.getCantidad() + cantidadASumar);
        return productoRepository.save(producto);
    }

    public Producto descontarCantidad(String codigoProducto, int cantidadADescontar) {
        logger.info("Descontando {} unidades del producto: {}", cantidadADescontar, codigoProducto);

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
        logger.info("Eliminando producto: {}", codigoProducto);

        Producto producto = productoRepository.findByCodigo(codigoProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto != null) {
            productoRepository.delete(producto);
        }
    }
}
