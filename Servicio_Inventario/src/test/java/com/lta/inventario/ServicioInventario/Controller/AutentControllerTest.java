package com.lta.inventario.ServicioInventario.Controller;

import com.lta.inventario.ServicioInventario.Inventario.InventarioService;
import com.lta.inventario.ServicioInventario.Inventario.Producto;
import com.lta.inventario.ServicioInventario.Inventario.ProductoRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutentControllerTest {

    @Mock
    private AutentService autentService;

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private AutentController controller;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .codigo("P001")
                .nombreProducto("Cafe")
                .precio(2000)
                .cantidad(10)
                .build();
    }

    //TEST AL LOGIN
    @SuppressWarnings("null")
    @Test
    void deberiaHacerLogin() {
        LoginRequest request = new LoginRequest("omar", "123");

        AutentResponse responseMock = new AutentResponse("token123");

        when(autentService.login(request)).thenReturn(responseMock);

        ResponseEntity<AutentResponse> response = controller.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("token123", response.getBody().getToken());
    }

    //TEST A AGREGAR PRODUCTO
    @SuppressWarnings("null")
    @Test
    void deberiaAgregarProducto() {
        ProductoRequest request = new ProductoRequest("P001", "Cafe", 2000, 10);

        when(inventarioService.agregaProducto(request)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.agregaProducto(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("P001", response.getBody().getCodigo());
    }

    //TEST A LISTAR PRODUCTOS
    @SuppressWarnings("null")
    @Test
    void deberiaListarProductos() {
        when(inventarioService.obtenerProductos()).thenReturn(List.of(producto));

        ResponseEntity<List<Producto>> response = controller.listadoProductos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    //TEST A SUMAR PRODUCTO
    @SuppressWarnings("null")
    @Test
    void deberiaSumarProducto() {
        when(inventarioService.sumarAProducto("P001", 5)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.sumarProducto("P001", 5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("P001", response.getBody().getCodigo());
    }

    //TEST A DESCONTAR PRODUCTO
    @SuppressWarnings("null")
    @Test
    void deberiaDescontarProducto() {
        when(inventarioService.descontarCantidad("P001", 5)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.descontarCantidad("P001", 5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("P001", response.getBody().getCodigo());
    }

    //TEST A ELIMINAR PRODUCTO
    @Test
    void deberiaEliminarProducto() {
        doNothing().when(inventarioService).eliminarProducto("P001");

        ResponseEntity<Void> response = controller.eliminarProducto("P001");

        assertEquals(204, response.getStatusCodeValue());

        verify(inventarioService).eliminarProducto("P001");
    }
}