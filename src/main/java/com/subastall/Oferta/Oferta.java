package com.irojas.demojwt.Oferta;

import com.irojas.demojwt.User.User;
import com.irojas.demojwt.Subasta.Subasta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oferta", schema = "public")
public class Oferta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subasta_id", nullable = false)
    private Subasta subasta;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private boolean esGanadora = false; // Indica si es la oferta ganadora

    @Column(nullable = false)
    private boolean esCancelada = false;
}
