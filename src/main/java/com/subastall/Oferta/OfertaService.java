package com.irojas.demojwt.Oferta;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irojas.demojwt.Subasta.SubastaRepository;
import com.irojas.demojwt.User.User;
import com.irojas.demojwt.Subasta.Subasta;
import com.irojas.demojwt.Subasta.Subasta.EstadoSubasta;
import com.irojas.demojwt.User.UserRepository;

import java.util.List;
import java.util.ArrayList;

//error http
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OfertaService {

    private OfertaRepository ofertaRepository;
    private SubastaRepository subastaRepository;
    private UserRepository userRepository;

    public OfertaService(OfertaRepository ofertaRepository, SubastaRepository subastaRepository, UserRepository userRepository) {
        this.ofertaRepository = ofertaRepository;
        this.subastaRepository = subastaRepository;
        this.userRepository = userRepository;
    }

    public List<Oferta> listarOfertasPorSubasta(Long subastaId) {
        return ofertaRepository.findAll(); // Cambia esto por una consulta personalizada
    }

    public OfertaDTO crearOferta(CrearOfertaRequest request, int usuarioId) {
        // Validar subasta
        Subasta subasta = subastaRepository.findById(request.getSubastaId()).orElse(null);
        if (subasta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subasta no encontrada");
        }

        if (!EstadoSubasta.ACTIVA.equals(subasta.getEstado())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La subasta no está activa.");
        }

        if (request.getMonto() <= subasta.getPrecioActual()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La oferta debe ser mayor al precio actual.");
        }
        subasta.setPrecioActual(request.getMonto());

        // Crear la entidad Oferta
        Oferta oferta = Oferta.builder()
                .subasta(subasta)
                .user(userRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .monto(request.getMonto())
                .build();

        // Guardar la oferta en el repositorio
        Oferta ofertaGuardada = ofertaRepository.save(oferta);
        subastaRepository.save(subasta);
        // Convertir la entidad guardada en DTO
        return new OfertaDTO(ofertaGuardada);
    }

    public void cancelarOferta(Long ofertaId, int usuarioId) {
        Oferta oferta = ofertaRepository.findById(ofertaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Oferta no encontrada"));

        if(oferta.getUser().getId() == usuarioId){
            oferta.setEsCancelada(true);
            ofertaRepository.save(oferta);
            Subasta subasta = subastaRepository.findById(oferta.getSubasta().getId()).orElse(null);
            // Subasta subasta = oferta.getSubasta();
            if(subasta != null){
                Oferta mejor_oferta = ofertaRepository.findTopBySubastaOrderByMontoDesc(subasta.getId()).orElse(null);
                if (mejor_oferta != null){
                    subasta.setPrecioActual(mejor_oferta.getMonto());
                    subastaRepository.save(subasta);
                }
                //se actualiza el precio por si la oferta cancelada es la mayor
            }
            
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta oferta no te pertenece");
        }
    }

    public void verificar_ganadora(Oferta oferta){
        Subasta subasta = subastaRepository.findById(oferta.getSubasta().getId()).orElse(null);
        // Subasta subasta = oferta.getSubasta();
        if (subasta !=  null){
            Oferta mejor_oferta = ofertaRepository.findTopBySubastaOrderByMontoDesc(subasta.getId()).orElse(null);
            if (subasta.getEstado() == EstadoSubasta.FINALIZADA && mejor_oferta != null && oferta == mejor_oferta ) {
                oferta.setEsGanadora(true);
                ofertaRepository.save(oferta);
            }
        }
    }

    public OfertaDTO obtenerOfertaPorId(Long id){
        Oferta oferta = ofertaRepository.findById(id).orElse(null);
        if(oferta == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta oferta no existe");
        }
        verificar_ganadora(oferta);
        return new OfertaDTO(oferta);
    }

    public List<OfertaDTO> obtenerOfertasPorUsuario(int id) {
        User usuario = userRepository.findById(id).orElse(null);
        if(usuario == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no existe");
        }
        List<Oferta> ofertas = ofertaRepository.findByUser(usuario);

        // Verificar si cada oferta es ganadora
        for (Oferta oferta : ofertas) {
            verificar_ganadora(oferta);
        }
        // Llama al constructor de OfertaDTO que toma una Oferta
        return  ofertas.stream().map(OfertaDTO::new).toList();
    }

    public List<OfertaDTO> obtenerOfertasPorSubasta(long id) {
        Subasta subasta = subastaRepository.findById(id).orElse(null);
        if(subasta == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La subasta no existe");
        }
        List<Oferta> ofertas =  ofertaRepository.findBySubasta(subasta);
        if (ofertas.isEmpty()) {
            return new ArrayList<>();
        }

        // Verificar si cada oferta es ganadora
        for (Oferta oferta : ofertas) {
            verificar_ganadora(oferta);
        }
        return ofertas.stream().map(OfertaDTO::new).toList();
    }

    public OfertaDTO obtenerMejorOfertaPorSubasta(long id) {
        Subasta subasta = subastaRepository.findById(id).orElse(null);
        if(subasta == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La subasta no existe");
        }
        Oferta oferta = ofertaRepository.findTopBySubastaOrderByMontoDesc(subasta.getId()).orElse(null);
        // Verificar si cada oferta es ganadora
        // if (oferta == null){
        //     return OfertaDTO();
        // }
        verificar_ganadora(oferta);
        
        return new OfertaDTO(oferta);
    }

    public int get_user_id(String usuario){
        User user=userRepository.findByUsername(usuario).orElseThrow();
        return user.getId();
    }
    // Puedes agregar más lógica si es necesario
}
