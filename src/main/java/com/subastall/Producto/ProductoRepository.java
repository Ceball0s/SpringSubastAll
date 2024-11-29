package com.irojas.demojwt.Producto;

import java.util.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// @Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Productos genéricos
    List<Producto> findTop10ByOrderByFechaCreacionDesc();

    // // Productos personalizados (consulta personalizada)
    // @Query("SELECT p FROM Producto p WHERE p.categoria.id IN " +
    //        "(SELECT pc.categoria.id FROM PreferenciasUsuario pc WHERE pc.usuario.id = :usuarioId)")
    
    // List<Producto> findRecomendadosPorUsuario(@Param("usuarioId") Long usuarioId);
    @Query("SELECT p FROM Producto p WHERE p.user.id = :userId")
    List<Producto> findRecomendadosPorUsuario(@Param("userId") int userId);

}
