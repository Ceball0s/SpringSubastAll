package com.irojas.demojwt.Subasta;

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
import com.irojas.demojwt.Subasta.SubastaService;
import com.irojas.demojwt.Subasta.SubastaDTO;
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
@RequestMapping("/api/subasta")//Subasta
@CrossOrigin(origins = {"http://localhost:5173"})
public class SubastaController {

    private final SubastaService subastaService;
    private final JwtService jwtService;
    // private final UserRepository userRepository;
    

    public SubastaController(SubastaService subastaService, JwtService jwtService ) {
        this.subastaService = subastaService;
        this.jwtService = jwtService;
        // this.userRepository = userRepository;
    }

    @GetMapping("/recomendaciones")
    public ResponseEntity<List<SubastaDTO>> obtenerRecomendaciones(@RequestHeader(value = "Authorization", required = false) String token) {
        List<SubastaDTO> subastas;
        
        if (!esTokenValido(token)) {
            subastas = subastaService.obtenerRecomendacionesGenericas();
        } else {
            //int usuarioId = extraerUsuarioDeToken(token);
            // subastas = subastaService.obtenerRecomendacionesPersonalizadas(usuarioId);
            subastas = subastaService.obtenerRecomendacionesGenericas();
        }

        return ResponseEntity.ok(subastas);
    }

    @PostMapping("/agregar")
    public ResponseEntity<SubastaDTO> agregarSubasta(
            @RequestBody AgregarRequest request, 
            @RequestHeader(value = "Authorization", required = true) String token) {
        if (!esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        System.out.println("verifique");
        // Extrae el ID del usuario desde el token
        int usuarioId = extraerUsuarioDeToken(token);
        //agrega la subasta segun el id del usuario que iso la peticion
        SubastaDTO subastaGuardado = subastaService.agregarSubasta(request, usuarioId);
        return ResponseEntity.ok(subastaGuardado);
    }

    @GetMapping("/foto/{nombreArchivo}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable String nombreArchivo) {
        if (nombreArchivo.contains("..") || nombreArchivo.contains("/")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // probir subir directorio
        }
        byte[] fotoBytes = subastaService.obtenerFoto(nombreArchivo);
        // Si los bytes están vacíos, retornamos un 404 Not Found, indicando que no se encontró la foto
        if (fotoBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Si la foto existe, la devolvemos con el tipo adecuado (puede ser JPEG, PNG, etc.)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // O el tipo adecuado según el archivo
            .body(fotoBytes);

    }

    @GetMapping("/{subastaId}")
    public ResponseEntity<SubastaDTO> consultarSubasta(@PathVariable int subastaId) {
        // cualquier usuario puede ver cualquier subasta
        // por ahora al ser un proyecto de prueba no hay protecion de datos al enviar la
        // consulta en otro caso se deberia cambiar el bojeto DTO para progeger mas la
        // seguridad del usuario
        // int usuarioId = extraerUsuarioDeToken(token);

        // Llamamos al servicio para modificar la subasta
        SubastaDTO subastaModificada = subastaService.obtenerSubastaPorId(subastaId);
        //mostrar la modificacion
        return ResponseEntity.ok(subastaModificada);
    }
    @CrossOrigin(origins = "*")
    @PutMapping("/{subastaId}")
    public ResponseEntity<SubastaDTO> modificarSubasta(@PathVariable int subastaId,
                                                       @RequestBody AgregarRequest request,
                                                       @RequestHeader("Authorization") String token) {
        if (!esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        
        // Extraemos el usuario ID desde el token
        int usuarioId = extraerUsuarioDeToken(token);

        // Llamamos al servicio para modificar la subasta
        SubastaDTO subastaModificada = subastaService.modificarSubasta(subastaId, request, usuarioId);

        return ResponseEntity.ok(subastaModificada);
    }

    @PutMapping("/finalizar/{subastaId}")
    public ResponseEntity<SubastaDTO> cerrarSubasta(@PathVariable int subastaId,
                                                    @RequestHeader("Authorization") String token) {
                                                    
        if (!esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        int usuarioId = extraerUsuarioDeToken(token);
        SubastaDTO subastaCerrada = subastaService.cerrarSubasta(subastaId, usuarioId);
        return ResponseEntity.ok(subastaCerrada);
    }

    @PutMapping("/cancelar/{subastaId}")
    public ResponseEntity<SubastaDTO> cancelarSubasta(@PathVariable int subastaId,
                                                      @RequestHeader("Authorization") String token) {
        if (!esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        int usuarioId = extraerUsuarioDeToken(token);
        SubastaDTO subastaCancelada = subastaService.cancelarSubasta(subastaId, usuarioId);
        return ResponseEntity.ok(subastaCancelada);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<SubastaDTO>> obtener_Subastas(@RequestHeader("Authorization") String token) {
        if (!esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        int usuarioId = extraerUsuarioDeToken(token);
        List<SubastaDTO> subastas = subastaService.obtenerSubastasPorUsuario(usuarioId);
        return ResponseEntity.ok(subastas);
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
            return this.subastaService.get_user_id(this.jwtService.getUsernameFromToken(token.substring(7)));
        } else {
            return this.subastaService.get_user_id(this.jwtService.getUsernameFromToken(token));
        }
         
    }

}

// private String extraerUsuarioDeToken(String token) {
    //     // Extrae el ID del usuario desde el token JWT
    //     // return this.jwtService.obtenerClaim(token, "id");
    //     String username = this.jwtService.getUsernameFromToken(token);
        
    //     UserDTO user= this.userService.getUser(username).orElse(null);

    //     if (user!=null)
    //     {
    //         return userDTO;
    //     }
    //     else
    //     {
    //         return null;
    //     }
        
    // }
