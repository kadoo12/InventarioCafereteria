package com.lta.inventario.ServicioInventario.Inventario;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventarioService = new InventarioService(inventarioRepository);
    }

    @SuppressWarnings("null")
    @Test
    void deberiaAgregarProducto() {
        ProductoRequest productoRequest = new ProductoRequest("P001", "CHOCOLATE", 1000, 10);
        
        when(inventarioRepository.findByCodigo("P001")).thenReturn(java.util.Optional.empty());

        Producto productoGuardado =  Producto.builder()
                .codigo("P001")
                .nombreProducto("CHOCOLATE")
                .precio(1000)
                .cantidad(10)
                .build();

        when(inventarioRepository.save(org.mockito.ArgumentMatchers.any(Producto.class))).thenReturn(productoGuardado);
        Producto resultado = inventarioService.agregaProducto(productoRequest);

        assertEquals("P001", resultado.getCodigo());
        assertEquals("CHOCOLATE", resultado.getNombreProducto());

}

@Test
void debeObtenerProductos() {
        Producto producto =  Producto.builder()
                .codigo("P001")
                .nombreProducto("CHOCOLATE")
                .precio(1000)
                .cantidad(10)
                .build();

        when(inventarioRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = inventarioService.obtenerProductos();

        assertEquals(1, resultado.size());
        assertEquals("P001", resultado.get(0).getCodigo());
}   

@SuppressWarnings("null")
@Test
void deberiaSumarCantidadSiProductoExiste() {

ProductoRequest request = new ProductoRequest("P002","Cafe",2000,5);

Producto productoExistente = Producto.builder()
            .codigo("P002")
            .nombreProducto("Cafe")
            .precio(2000)
            .cantidad(10)
            .build();

    when(inventarioRepository.findByCodigo("P002"))
            .thenReturn(Optional.of(productoExistente));

    when(inventarioRepository.save(any())).thenReturn(productoExistente);

    Producto resultado = inventarioService.agregaProducto(request);

    assertEquals(15, resultado.getCantidad());
    
}

@SuppressWarnings("null")
@Test
void deberiaSumarCantidadAProducto() {

    Producto producto = Producto.builder()
            .codigo("P003")
            .nombreProducto("Galletas")
            .precio(2000)
            .cantidad(10)
            .build();

    when(inventarioRepository.findByCodigo("P003"))
            .thenReturn(Optional.of(producto));

    when(inventarioRepository.save(any())).thenReturn(producto);

    Producto resultado = inventarioService.sumarAProducto("P003", 5);

    assertEquals(15, resultado.getCantidad());
}
}