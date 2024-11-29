package com.irojas.demojwt.API_Producto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import lombok.RequiredArgsConstructor;
import com.irojas.demojwt.Producto.ProductoService;
import com.irojas.demojwt.Producto.ProductoDTO;
import com.irojas.demojwt.Jwt.JwtService;
// import com.irojas.demojwt.User.User;
import java.util.List;
// UserRepository
import org.springframework.web.bind.annotation.PathVariable;

//fotos y tipo de estados http
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/api/productos")
//@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class ProductoController {

    private final ProductoService productoService;
    private final JwtService jwtService;
    // private final UserRepository userRepository;
    

    public ProductoController(ProductoService productoService, JwtService jwtService ) {
        this.productoService = productoService;
        this.jwtService = jwtService;
        // this.userRepository = userRepository;
    }

    @GetMapping("/recomendaciones")
    public ResponseEntity<List<ProductoDTO>> obtenerRecomendaciones(@RequestHeader(value = "Authorization", required = false) String token) {
        List<ProductoDTO> productos;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } 
        if (token == null || !esTokenValido(token)) {
            productos = productoService.obtenerRecomendacionesGenericas();
        } else {
            int usuarioId = extraerUsuarioDeToken(token);
            
            productos = productoService.obtenerRecomendacionesPersonalizadas(usuarioId);
            // productos = productoService.obtenerRecomendacionesGenericas();
        }

        return ResponseEntity.ok(productos);
    }

    @PostMapping("/agregar")
    public ResponseEntity<ProductoDTO> agregarProducto(
            @RequestBody AgregarRequest request, 
            @RequestHeader(value = "Authorization", required = true) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } 
        if (token == null || !esTokenValido(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso denegado
        }
        // Extrae el ID del usuario desde el token
        int usuarioId = extraerUsuarioDeToken(token);
        //agrega la subasta segun el id del usuario que iso la peticion
        ProductoDTO productoGuardado = productoService.agregarProducto(request, usuarioId);
        return ResponseEntity.ok(productoGuardado);
    }

    @GetMapping("/producto/foto/{nombreArchivo}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable String nombreArchivo) {
        if (nombreArchivo.contains("..") || nombreArchivo.contains("/")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // probir subir directorio
        }
        byte[] fotoBytes = productoService.obtenerFoto(nombreArchivo);
        // Si los bytes están vacíos, retornamos un 404 Not Found, indicando que no se encontró la foto
        if (fotoBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Si la foto existe, la devolvemos con el tipo adecuado (puede ser JPEG, PNG, etc.)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // O el tipo adecuado según el archivo
            .body(fotoBytes);

    }



    private boolean esTokenValido(String token) {
        // UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        // Valida el token JWT
        // int usuarioId = extraerUsuarioDeToken(token);
        // User user = productoService.get_user(usuarioId);
        return this.jwtService.isTokenValid(token);
    }

    private int extraerUsuarioDeToken(String token) {
        return this.productoService.get_user_id(this.jwtService.getUsernameFromToken(token));
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
}
