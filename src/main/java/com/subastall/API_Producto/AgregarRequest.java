package com.irojas.demojwt.API_Producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.irojas.demojwt.User.User;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgregarRequest {
    private String nombre;
    private String descripcion;
    private Double precioInicial;
    private String fotoBase64;
}
