package com.lta.inventario.ServicioInventario.Inventario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.lta.inventario.ServicioInventario.Categoria.Categoria;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Producto", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Producto {

    @Id
    @GeneratedValue 
    @Column(name = "id_producto")  
    int idProducto;

    @Column(nullable = false, unique = true)
    String codigo;

    @Column(nullable = false)
    String nombreProducto;

    int precio;

    int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = true)
    Categoria categoria;
}
