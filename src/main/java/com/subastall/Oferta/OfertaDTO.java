package com.irojas.demojwt.Oferta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;
import java.util.Date;
import com.irojas.demojwt.Subasta.SubastaDTO;
import com.irojas.demojwt.Subasta.UserDTOP;
import com.irojas.demojwt.Oferta.Oferta;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDTO {
    Long id;
    Double monto;
    boolean esGanadora;
    SubastaDTO subasta;
    UserDTOP usuario_oferta;


    public OfertaDTO(Oferta oferta){
        this.id = oferta.getId();
        this.monto = oferta.getMonto();
        this.esGanadora = oferta.isEsGanadora();
        this.subasta = new SubastaDTO(oferta.getSubasta());
        this.usuario_oferta = new UserDTOP(oferta.getUser());
    }
}
