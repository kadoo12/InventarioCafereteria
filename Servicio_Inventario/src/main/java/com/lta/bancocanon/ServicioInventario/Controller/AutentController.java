/*
 * La clase controller expone las al API REST para realizar el debido proceso de registro e inicio de sesion
 * Adicional, expone el endpoint para cambiar la contraseña.
 */

package com.lta.bancocanon.ServicioInventario.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/controller")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080/")
public class AutentController {

    private final AutentService autentService;

    @PostMapping(value = "/login")
    public ResponseEntity<AutentResponse> login(@RequestBody LoginRequest loginRequest){
        System.out.println("ENTRANDO A LOGIN CONTROLLER");
        return ResponseEntity.ok(autentService.login(loginRequest));
    }

    
}
