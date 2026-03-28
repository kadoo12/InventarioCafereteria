/*
 * La clase controller expone las al API REST para realizar el debido proceso de registro e inicio de sesion
 * Adicional, expone el endpoint para cambiar la contraseña.
 */

package com.lta.inventario.ServicioInventario.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lta.inventario.ServicioInventario.Inventario.Producto;
import com.lta.inventario.ServicioInventario.Inventario.ProductoRequest;
import com.lta.inventario.ServicioInventario.Inventario.InventarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/controller")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080/")
public class AutentController {

    private final AutentService autentService;
    private final InventarioService inventarioService;

    @PostMapping(value = "/login")
    public ResponseEntity<AutentResponse> login(@RequestBody LoginRequest loginRequest){
        System.out.println("ENTRANDO A LOGIN CONTROLLER");
        return ResponseEntity.ok(autentService.login(loginRequest));
    }

    @PostMapping(value = "/inventario/agregarProducto")
    public ResponseEntity<Producto> agregaProducto(@RequestBody ProductoRequest productoRequest){
        System.out.println("ENTRANDO A AGREGAR PRODUCTO CONTROLLER");
        return ResponseEntity.ok(inventarioService.agregaProducto(productoRequest));
    }

    @GetMapping(value = "/inventario/listadoProductos")
    public ResponseEntity<List<Producto>> listadoProductos(){
        System.out.println("ENTRANDO A LISTADO PRODUCTOS CONTROLLER");
        return ResponseEntity.ok(inventarioService.obtenerProductos());
    }

    @PutMapping(value = "/inventario/sumarProducto/{codigo}/{cantidad}")
    public ResponseEntity<Producto> sumarProducto(
        @PathVariable String codigo,
        @PathVariable int cantidad){
        System.out.println("ENTRANDO A SUMAR PRODUCTO CONTROLLER");
        return ResponseEntity.ok(inventarioService.sumarAProducto(codigo, cantidad));

    }
    @PutMapping(value = "/inventario/descontarCantidad/{codigo}/{cantidad}")
    public ResponseEntity<Producto> descontarCantidad(
        @PathVariable String codigo,
        @PathVariable int cantidad){
        System.out.println("ENTRANDO A DESCONTAR PRODUCTO CONTROLLER");
        return ResponseEntity.ok(inventarioService.descontarCantidad(codigo, cantidad));

    }
    @DeleteMapping(value = "/inventario/eliminarProducto/{codigo}")
    public ResponseEntity<Void> eliminarProducto(
        @PathVariable String codigo){
        System.out.println("ENTRANDO A ELIMINAR PRODUCTO CONTROLLER");
        inventarioService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build();
    }

}
