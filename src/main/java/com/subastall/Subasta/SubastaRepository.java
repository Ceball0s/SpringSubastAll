package com.irojas.demojwt.Subasta;

import java.util.Optional;
import com.irojas.demojwt.User.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// @Repository
public interface SubastaRepository extends JpaRepository<Subasta, Long> {

    // Subastas genéricos
    List<Subasta> findTop10ByOrderByFechaCreacionDesc();

    // // Subastas personalizados (consulta personalizada)
    // @Query("SELECT p FROM Subasta p WHERE p.categoria.id IN " +
    //        "(SELECT pc.categoria.id FROM PreferenciasUsuario pc WHERE pc.usuario.id = :usuarioId)")
    
    //List<Subasta> findRecomendadosPorUsuario(@Param("usuarioId") Long usuarioId);


    // @Query("SELECT p FROM subasta p WHERE p.user.id = :userId")
    // List<Subasta> findRecomendadosPorUsuario(@Param("userId") int userId);

    Optional<Subasta> findById(long id);

    List<Subasta> findByUser(User user);
    
    @Query("SELECT s FROM Subasta s WHERE s.fechaCierre > CURRENT_TIMESTAMP")
    List<Subasta> findSubastasAbiertas();
}
