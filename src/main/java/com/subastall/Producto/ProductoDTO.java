package com.irojas.demojwt.Producto;

import com.irojas.demojwt.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;
import java.util.Date;
import com.irojas.demojwt.Producto.UserDTOP;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precioInicial;
    private List<String> fotos; // Lista de rutas de las fotos
    private Date fechaCreacion;
    private UserDTOP user;

    // Constructor para convertir la foto binaria a Base64
    public ProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precioInicial = producto.getPrecioInicial();
        this.fotos = producto.getFotos();
        this.fechaCreacion = producto.getFechaCreacion();
        this.user = new UserDTOP(
                producto.getUser().getId(),
                producto.getUser().getUsername(),
                producto.getUser().getLastname(),
                producto.getUser().getFirstname(),
                producto.getUser().getCountry(),
                producto.getUser().getDate(),
                producto.getUser().getGenter()
        );
    }

    // Getters y setters
}
