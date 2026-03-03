package com.lta.bancocanon.ServicioInventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServicioInventario {

	/*
	 * CLASE MAIN PARA EJECUTAR PROGRAMA
	 */
	public  static void main(String[] args) {
		System.out.println("Ejecutando Servicio Inventario...");
		SpringApplication.run(ServicioInventario.class, args);
	
	}

}
