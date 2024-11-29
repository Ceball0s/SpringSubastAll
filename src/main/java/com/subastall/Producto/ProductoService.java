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

        // Convierte el DTO a la entidad Producto
        Producto producto = Producto.builder()
                .nombre(productoDTO.getNombre())
                .descripcion(productoDTO.getDescripcion())
                .precioInicial(productoDTO.getPrecioInicial())
                .fechaCreacion(new Date())
                .foto(Base64.getDecoder().decode(productoDTO.getFotoBase64()))
                .user(user) // Asigna el usuario propietario
                .build();

        // Guarda el producto en la base de datos
        Producto productoGuardado = this.productoRepository.save(producto);

        // Devuelve el producto guardado como DTO
        return new ProductoDTO(productoGuardado);
    }

     public ProductoDTO agregarProducto(AgregarRequest request, int usuarioId) {

        User user= userRepository.findById(usuarioId).orElse(null);
        System.out.println("hola llege aqui");
        // Convierte el DTO a la entidad Producto
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precioInicial(request.getPrecioInicial())
                .fechaCreacion(new Date())
                .foto(Base64.getDecoder().decode(request.getFotoBase64()))
                .user(user) // Asigna el usuario propietario
                .build();

        // Guarda el producto en la base de datos
        Producto productoGuardado = this.productoRepository.save(producto);

        // Devuelve el producto guardado como DTO
        return new ProductoDTO(productoGuardado);
    }

    public int get_user_id(String usuario){
        User user=userRepository.findByUsername(usuario).orElseThrow();
        return user.getId();

    }


}
