package com.lta.inventario.ServicioInventario.Inventario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

String codigo;

@Column(nullable = false)
String nombreProducto;

int precio;

int cantidad;


}
