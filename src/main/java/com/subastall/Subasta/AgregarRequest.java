package com.irojas.demojwt.Subasta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.irojas.demojwt.User.User;
import java.util.Date;
import java.util.List;
import com.irojas.demojwt.Subasta.Subasta.EstadoSubasta;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgregarRequest {
    private String nombre;
    private String descripcion;
    private Double precioInicial;
    private List<String> fotos;
    private Date fechaCierre;
    private Double precioActual;
    private EstadoSubasta estado;
}

// {
//   "nombre": "Subasta de un cuadro famoso",
//   "descripcion": "Cuadro original de Van Gogh",
//   "precioInicial": 10000.0,
//   "fotos": ["ruta1.jpg", "ruta2.jpg"],
//   "fechaCierre": "2024-12-15T23:59:59"
// }
