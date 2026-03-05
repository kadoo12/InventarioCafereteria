package com.lta.inventario.ServicioInventario.Inventario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface InventarioRepository  extends JpaRepository<Producto,Integer> {
    Optional<Producto> findByNombreProducto(String nombreProducto);

    Optional<Producto> findByCodigo(String codigo);
}

