package com.irojas.demojwt.Oferta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.irojas.demojwt.User.User;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearOfertaRequest {
    private Long subastaId; // ID de la subasta a la que pertenece la oferta
    private Double monto;   // Monto de la oferta
}