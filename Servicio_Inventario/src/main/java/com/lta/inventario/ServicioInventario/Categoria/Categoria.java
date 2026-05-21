package com.lta.inventario.ServicioInventario.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categoria")
public class Categoria {
    
    @Id    
    @GeneratedValue 
    @Column(name = "id")
    Integer id;
    
    @Column(nullable = false, unique = true)
    String nombre;
    
    @Column(name = "descripcion")
    String descripcion;
}
