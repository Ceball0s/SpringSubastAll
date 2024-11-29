package com.irojas.demojwt.Producto;

import com.irojas.demojwt.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;
import java.util.Date;
import com.irojas.demojwt.User.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precioInicial;
    private String fotoBase64;
    private Date fechaCreacion;
    private User user;

    // Constructor para convertir la foto binaria a Base64
    public ProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precioInicial = producto.getPrecioInicial();
        this.fotoBase64 = Base64.getEncoder().encodeToString(producto.getFoto());
        this.fechaCreacion = producto.getFechaCreacion();
        this.user = producto.getUser();
    }

    // Getters y setters
}
