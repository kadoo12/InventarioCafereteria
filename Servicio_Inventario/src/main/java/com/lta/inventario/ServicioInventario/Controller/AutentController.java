/*
 * La clase controller expone las al API REST para realizar el debido proceso de registro e inicio de sesion
 * Adicional, expone el endpoint para cambiar la contraseña.
 */

package com.lta.inventario.ServicioInventario.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lta.inventario.ServicioInventario.Inventario.Producto;
import com.lta.inventario.ServicioInventario.Inventario.ProductoRequest;
import com.lta.inventario.ServicioInventario.Inventario.InventarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/controller")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class AutentController {

    private final AutentService autentService;
    private final InventarioService inventarioService;

    @PostMapping(value = "/login")
    public ResponseEntity<AutentResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(autentService.login(loginRequest));
    }

    @PostMapping(value = "/inventario/agregarProducto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> agregaProducto(@RequestBody ProductoRequest productoRequest){
        System.out.println("ProductoRequest recibido: " + productoRequest);
        return ResponseEntity.ok(inventarioService.agregaProducto(productoRequest));
    }

    @GetMapping(value = "/inventario/listadoProductos")
    public ResponseEntity<List<Producto>> listadoProductos(){
        return ResponseEntity.ok(inventarioService.obtenerProductos());
    }

    @GetMapping(value = "/inventario/listadoProductos/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> listadoProductosPorCategoria(@PathVariable Integer categoriaId){
        return ResponseEntity.ok(inventarioService.obtenerProductosPorCategoria(categoriaId));
    }

    @PutMapping(value = "/inventario/sumarProducto/{codigo}/{cantidad}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> sumarProducto(
        @PathVariable String codigo,
        @PathVariable int cantidad){
        return ResponseEntity.ok(inventarioService.sumarAProducto(codigo, cantidad));

    }
    
    @PutMapping(value = "/inventario/descontarCantidad/{codigo}/{cantidad}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<Producto> descontarCantidad(
        @PathVariable String codigo,
        @PathVariable int cantidad){
        return ResponseEntity.ok(inventarioService.descontarCantidad(codigo, cantidad));

    }
    
    @DeleteMapping(value = "/inventario/eliminarProducto/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(
        @PathVariable String codigo){
        inventarioService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/generarHash")
    public ResponseEntity<String> generarHash(@RequestParam String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        return ResponseEntity.ok(hash);
    }

}
