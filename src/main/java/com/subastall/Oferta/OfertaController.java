package com.irojas.demojwt.Oferta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import lombok.RequiredArgsConstructor;
import com.irojas.demojwt.Oferta.OfertaService;
import com.irojas.demojwt.Oferta.OfertaDTO;
import com.irojas.demojwt.Oferta.CrearOfertaRequest;
import com.irojas.demojwt.Jwt.JwtService;
// import com.irojas.demojwt.User.User;
import java.util.List;
// UserRepository
import org.springframework.web.bind.annotation.PathVariable;

//fotos y tipo de estados http
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


//@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ofertas")
@CrossOrigin(origins = {"http://localhost:5173"})
public class OfertaController {

    private final OfertaService ofertaService;
    private final JwtService jwtService;
    // private final UserRepository userRepository;
    

    public OfertaController(OfertaService ofertaService, JwtService jwtService ) {
        this.ofertaService = ofertaService;
        this.jwtService = jwtService;
        // this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaDTO> obtenerOfertaPorId(@PathVariable Long id) {
        try {
            OfertaDTO oferta = ofertaService.obtenerOfertaPorId(id);
            return ResponseEntity.ok(oferta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); // Oferta no encontrada
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<OfertaDTO>> obtenerOfertasPorUsuario(@RequestHeader("Authorization") String token) {
        if (!esTokenValido(token)) {
            return ResponseEntity.status(403).build(); // Acceso denegado
        }
        int usuarioId = extraerUsuarioDeToken(token); // Obtener ID del usuario desde el token
        // User usuario = userService.obtenerUsuarioPorId(usuarioId);

        List<OfertaDTO> ofertas = ofertaService.obtenerOfertasPorUsuario(usuarioId);
        return ResponseEntity.ok(ofertas);
    }

    @GetMapping("/subasta/{subastaId}")
    public ResponseEntity<List<OfertaDTO>> obtenerOfertasPorSubasta(@PathVariable Long subastaId) {
        try {
            List<OfertaDTO> ofertas = ofertaService.obtenerOfertasPorSubasta(subastaId);
            return ResponseEntity.ok(ofertas);
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); // Subasta no encontrada
        }
    }

    @GetMapping("/subasta/mejor/{subastaId}")
    public ResponseEntity<OfertaDTO> obtenerMejorOfertaPorSubasta(@PathVariable Long subastaId) {
        try {
            OfertaDTO oferta = ofertaService.obtenerMejorOfertaPorSubasta(subastaId);
            return ResponseEntity.ok(oferta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); // Subasta no encontrada
        }
    }


    @PostMapping("/crear")
    public ResponseEntity<OfertaDTO> crearOferta(@RequestBody CrearOfertaRequest request, 
                                                @RequestHeader("Authorization") String token) {
        if (!esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        int usuarioId = extraerUsuarioDeToken(token); // Obtener ID del usuario desde el token

        // Crear la oferta usando el servicio
        OfertaDTO nuevaOferta = ofertaService.crearOferta(request, usuarioId);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOferta);
    }

    @GetMapping("/cancelar/{ofertaid}")
    public ResponseEntity<Void> cancelarOferta(@PathVariable Long ofertaid, 
                                                @RequestHeader("Authorization") String token) {
        if (!esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        int usuarioId = extraerUsuarioDeToken(token); // Obtener ID del usuario desde el token

        // Crear la oferta usando el servicio
        ofertaService.cancelarOferta(ofertaid, usuarioId);

        return ResponseEntity.noContent().build();
    }


    private boolean esTokenValido(String token) {
        // Valida el token JWT
        if (token != null && token.startsWith("Bearer ")) {
            String tokenSinBearer = token.substring(7).trim();
            if (!this.jwtService.isTokenValid(tokenSinBearer)) {
                return false;
            }
        } 
        return true;
    }
    //retorna id del usuario
    private int extraerUsuarioDeToken(String token) {
         if (token != null && token.startsWith("Bearer ")) {
            return this.ofertaService.get_user_id(this.jwtService.getUsernameFromToken(token.substring(7)));
        } else {
            return this.ofertaService.get_user_id(this.jwtService.getUsernameFromToken(token));
        }
         
    }

}

