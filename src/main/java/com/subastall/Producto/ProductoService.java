package com.irojas.demojwt.Producto;

import java.util.List;

import com.irojas.demojwt.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.Base64;
import com.irojas.demojwt.User.User;
import com.irojas.demojwt.User.UserRepository;
import com.irojas.demojwt.API_Producto.AgregarRequest;

//archivos
import jakarta.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;



@Service
public class ProductoService {

    private final UserRepository userRepository; 
    private final ProductoRepository productoRepository;

    public ProductoService(UserRepository userRepository, ProductoRepository productoRepository) {
        this.userRepository = userRepository;
        this.productoRepository = productoRepository;
    }

    public List<ProductoDTO> obtenerRecomendacionesGenericas() {
        return productoRepository.findTop10ByOrderByFechaCreacionDesc()
                .stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> obtenerRecomendacionesPersonalizadas(int usuarioId) {
        // Buscar categorías preferidas del usuario y productos relacionados
        return productoRepository.findRecomendadosPorUsuario(usuarioId)
                .stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public ProductoDTO agregarProducto(ProductoDTO productoDTO, int usuarioId) {

        User user= userRepository.findById(usuarioId).orElse(null);

        List<String> rutasFotos = guardarFotos(productoDTO.getFotos());

        // Convierte el DTO a la entidad Producto
        Producto producto = Producto.builder()
                .nombre(productoDTO.getNombre())
                .descripcion(productoDTO.getDescripcion())
                .precioInicial(productoDTO.getPrecioInicial())
                .fechaCreacion(new Date())
                .fotos(rutasFotos)
                .user(user) // Asigna el usuario propietario
                .build();

        // Guarda el producto en la base de datos
        Producto productoGuardado = this.productoRepository.save(producto);

        // Devuelve el producto guardado como DTO
        return new ProductoDTO(productoGuardado);
    }

    public ProductoDTO agregarProducto(AgregarRequest request, int usuarioId) {

        User user= userRepository.findById(usuarioId).orElse(null);
        
        List<String> rutasFotos = guardarFotos(request.getFotos());

        // Convierte el DTO a la entidad Producto
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precioInicial(request.getPrecioInicial())
                .fechaCreacion(new Date())
                .fotos(rutasFotos)
                .user(user) // Asigna el usuario propietario
                .build();

        // Guarda el producto en la base de datos
        Producto productoGuardado = this.productoRepository.save(producto);

        // Devuelve el producto guardado como DTO
        return new ProductoDTO(productoGuardado);
    }

    private List<String> guardarFotos(List<String> fotosBase64) {
        List<String> rutasGuardadas = new ArrayList<>();
        try {
            for (String fotoBase64 : fotosBase64) {

                if (fotoBase64.contains(",")) {
                    fotoBase64 = fotoBase64.split(",")[1];
                }
                byte[] bytes = Base64.getDecoder().decode(fotoBase64);
                String nombreArchivo = System.currentTimeMillis() + ".jpg";
                Path rutaArchivo = Paths.get("uploads/fotos", nombreArchivo);

                // Crear directorios si no existen
                Files.createDirectories(rutaArchivo.getParent());
                Files.write(rutaArchivo, bytes);

                // Guardar la ruta como string
                rutasGuardadas.add(nombreArchivo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar fotos: " + e.getMessage());
        }
        return rutasGuardadas;
    }

    public byte[] obtenerFoto(String nombreArchivo) {
        String rutaFoto = "uploads/fotos/" + nombreArchivo;
        File file = new File(rutaFoto); // Ruta al archivo de la foto en el servidor

        // Si el archivo no existe o está vacío, retornamos un arreglo vacío
        if (!file.exists() || file.length() == 0) {
            return new byte[0]; // Foto vacía
        }

        try {
            // Leemos el archivo en bytes
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            // En caso de error al leer el archivo, devolvemos un arreglo vacío
            return new byte[0]; // Foto vacía
        }
    }



    public int get_user_id(String usuario){
        User user=userRepository.findByUsername(usuario).orElseThrow();
        return user.getId();

    }


}
