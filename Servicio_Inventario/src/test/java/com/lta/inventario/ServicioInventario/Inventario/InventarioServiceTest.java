package com.lta.inventario.ServicioInventario.Inventario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
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

    //OBTENER PRODUCTOS
    @Test
    void debeObtenerProductos() {
        Producto producto = Producto.builder()
                .codigo("P001")
                .nombreProducto("CHOCOLATE")
                .precio(1000)
                .cantidad(10)
                .build();

        when(inventarioRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = inventarioService.obtenerProductos();

        assertEquals(1, resultado.size());
    }

    //AGREGAR NUEVO PRODUCTO
    @SuppressWarnings("null")
@Test
    void deberiaAgregarProductoNuevo() {
        ProductoRequest request = new ProductoRequest("P001", "CHOCOLATE", 1000, 10);

        when(inventarioRepository.findByCodigo("P001")).thenReturn(Optional.empty());
        when(inventarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Producto resultado = inventarioService.agregaProducto(request);

        assertEquals("P001", resultado.getCodigo());
    }

    //PRODUCTO EXISTENTE → SUMA CANTIDAD
    @SuppressWarnings("null")
@Test
    void deberiaSumarCantidadSiProductoExiste() {
        Producto existente = Producto.builder()
                .codigo("P002")
                .nombreProducto("Cafe")
                .precio(2000)
                .cantidad(10)
                .build();

        ProductoRequest request = new ProductoRequest("P002", "Cafe", 2000, 5);

        when(inventarioRepository.findByCodigo("P002")).thenReturn(Optional.of(existente));
        when(inventarioRepository.save(any())).thenReturn(existente);

        Producto resultado = inventarioService.agregaProducto(request);

        assertEquals(15, resultado.getCantidad());
    }

    //ERROR: MISMO CÓDIGO, DISTINTO NOMBRE
    @Test
    void deberiaLanzarErrorSiNombreNoCoincide() {
        Producto existente = Producto.builder()
                .codigo("P002")
                .nombreProducto("Cafe")
                .cantidad(10)
                .build();

        ProductoRequest request = new ProductoRequest("P002", "Te", 2000, 5);

        when(inventarioRepository.findByCodigo("P002")).thenReturn(Optional.of(existente));

        assertThrows(RuntimeException.class, () -> {
            inventarioService.agregaProducto(request);
        });
    }

    //ERROR: PRODUCTO NO EXISTE AL SUMAR
    @Test
    void deberiaLanzarErrorSiProductoNoExisteAlSumar() {
        when(inventarioRepository.findByCodigo("P003")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            inventarioService.sumarAProducto("P003", 5);
        });
    }

    //SUMAR CANTIDAD CORRECTO
    @SuppressWarnings("null")
@Test
    void deberiaSumarCantidadAProducto() {
        Producto producto = Producto.builder()
                .codigo("P003")
                .cantidad(10)
                .build();

        when(inventarioRepository.findByCodigo("P003")).thenReturn(Optional.of(producto));
        when(inventarioRepository.save(any())).thenReturn(producto);

        Producto resultado = inventarioService.sumarAProducto("P003", 5);

        assertEquals(15, resultado.getCantidad());
    }

    //ERROR: CANTIDAD NEGATIVA
    @Test
    void deberiaLanzarErrorSiCantidadNegativa() {
        Producto producto = Producto.builder()
                .codigo("P004")
                .cantidad(10)
                .build();

        when(inventarioRepository.findByCodigo("P004")).thenReturn(Optional.of(producto));

        assertThrows(RuntimeException.class, () -> {
            inventarioService.descontarCantidad("P004", -5);
        });
    }

    //ERROR: STOCK INSUFICIENTE
    @Test
    void deberiaLanzarErrorSiStockInsuficiente() {
        Producto producto = Producto.builder()
                .codigo("P005")
                .cantidad(5)
                .build();

        when(inventarioRepository.findByCodigo("P005")).thenReturn(Optional.of(producto));

        assertThrows(RuntimeException.class, () -> {
            inventarioService.descontarCantidad("P005", 10);
        });
    }

    //DESCONTAR CORRECTAMENTE
    @SuppressWarnings("null")
@Test
    void deberiaDescontarCantidadCorrectamente() {
        Producto producto = Producto.builder()
                .codigo("P006")
                .cantidad(10)
                .build();

        when(inventarioRepository.findByCodigo("P006")).thenReturn(Optional.of(producto));
        when(inventarioRepository.save(any())).thenReturn(producto);

        Producto resultado = inventarioService.descontarCantidad("P006", 5);

        assertEquals(5, resultado.getCantidad());
    }
}