package com.irojas.demojwt.Oferta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irojas.demojwt.Subasta.SubastaRepository;
import com.irojas.demojwt.User.User;
import com.irojas.demojwt.Subasta.Subasta;
import com.irojas.demojwt.Subasta.Subasta.EstadoSubasta;

import java.util.List;

@Service
public class OfertaService {

    @Autowired
    private OfertaRepository ofertaRepository;
    private SubastaRepository subastaRepository;

    public List<Oferta> listarOfertasPorSubasta(Long subastaId) {
        return ofertaRepository.findAll(); // Cambia esto por una consulta personalizada
    }

    public Oferta crearOferta(Oferta oferta) {
        Subasta subasta = subastaRepository.findById(oferta.getSubasta().getId())
                            .orElseThrow(() -> new RuntimeException("subasta no encontrado"));

        if (!EstadoSubasta.ACTIVA.equals(subasta.getEstado())) {
            throw new RuntimeException("La subasta no está activa.");
        }

        if (oferta.getMonto() <= subasta.getPrecioActual()) {
            throw new RuntimeException("La oferta debe ser mayor al precio actual.");
        }

        subasta.setPrecioActual(oferta.getMonto());
        subastaRepository.save(subasta);

        return ofertaRepository.save(oferta);
    }

    public List<Oferta> obtenerOfertasPorUsuario(User usuario) {
        return ofertaRepository.findByUsuario(usuario);
    }

    public List<Oferta> obtenerOfertasPorSubasta(Subasta subasta) {
        return ofertaRepository.findBySubasta(subasta);
    }


    public void eliminarOferta(Long ofertaId) {
        ofertaRepository.deleteById(ofertaId);
    }

    // Puedes agregar más lógica si es necesario
}
