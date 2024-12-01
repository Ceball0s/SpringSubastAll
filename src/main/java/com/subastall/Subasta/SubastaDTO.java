package com.irojas.demojwt.Subasta;

import com.irojas.demojwt.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;
import java.util.Date;
import com.irojas.demojwt.Subasta.UserDTOP;
import com.irojas.demojwt.Subasta.Subasta.EstadoSubasta;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubastaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precioInicial;
    private Double precioActual;
    private List<String> fotos;
    private Date fechaCreacion;
    private Date fechaCierre;
    private String estado;
    private UserDTOP usuario_subasta;
    // private UserDTOP user_oferta_actual; // Puede ser null

    public SubastaDTO(Subasta subasta) {
        this.id = subasta.getId();
        this.nombre = subasta.getNombre();
        this.descripcion = subasta.getDescripcion();
        this.precioInicial = subasta.getPrecioInicial();
        this.precioActual = subasta.getPrecioActual();
        this.fotos = subasta.getFotos();
        this.fechaCreacion = subasta.getFechaCreacion();
        this.fechaCierre = subasta.getFechaCierre();
        
        this.usuario_subasta = new UserDTOP(subasta.getUser());

        if (subasta.getEstado() == EstadoSubasta.ACTIVA){
            this.estado = "ACTIVA";
        } else if (subasta.getEstado() == EstadoSubasta.FINALIZADA) {
            this.estado = "FINALIZADA";
        } else{
            this.estado = "CANCELADA";
        }
    }
}
