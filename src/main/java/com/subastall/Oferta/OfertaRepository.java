package com.irojas.demojwt.Oferta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.irojas.demojwt.User.User;
import com.irojas.demojwt.Subasta.Subasta;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    List<Oferta> findBySubasta(Subasta subasta);
    List<Oferta> findByUser(User user);
    Optional<Oferta> findById(Long id);
    
    @Query("SELECT o FROM Oferta o WHERE o.subasta.id = :subastaId AND o.esCancelada = false ORDER BY o.monto DESC")
    Optional<Oferta> findTopBySubastaOrderByMontoDesc(@Param("subastaId") Long subastaId);

}
