package com.lta.inventario.ServicioInventario.Categoria;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    private final CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerCategorias() {
        logger.debug("Obteniendo todas las categorías");
        return categoriaRepository.findAll();
    }

    public Categoria obtenerCategoriaPorId(Integer id) {
        logger.debug("Obteniendo categoría con id: {}", id);
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria crearCategoria(Categoria categoria) {
        logger.info("Creando nueva categoría: {}", categoria.getNombre());
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Integer id, Categoria categoriaActualizada) {
        logger.info("Actualizando categoría con id: {}", id);
        return categoriaRepository.findById(id)
            .map(categoria -> {
                categoria.setNombre(categoriaActualizada.getNombre());
                categoria.setDescripcion(categoriaActualizada.getDescripcion());
                return categoriaRepository.save(categoria);
            })
            .orElse(null);
    }

    public void eliminarCategoria(Integer id) {
        logger.info("Eliminando categoría con id: {}", id);
        categoriaRepository.deleteById(id);
    }
}
