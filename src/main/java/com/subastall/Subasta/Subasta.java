package com.irojas.demojwt.Subasta;

import com.irojas.demojwt.User.User;
import com.irojas.demojwt.Oferta.Oferta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subasta", schema = "public")
public class Subasta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSubasta estado;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double precioInicial;

    @Column()
    private Double precioActual;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;

    @ElementCollection
    @CollectionTable(name = "subasta_fotos", joinColumns = @JoinColumn(name = "subasta_id"))
    @Column(name = "ruta")
    private List<String> fotos;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user; // Relación con el usuario propietario del Subasta
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @ManyToOne
    // @JoinColumn(name = "user_oferta_actual_id", nullable = true)
    // // @JsonBackReference
    // private User user_oferta_actual;

    @PrePersist
    public void initializeFields() {
        // this.user_oferta_actual = null;
        this.estado = EstadoSubasta.ACTIVA; //
        this.precioActual = this.precioInicial; // Si deseas que precioActual también inicie igual al precio inicial
    }

    public enum EstadoSubasta {
        ACTIVA, FINALIZADA, CANCELADA
    }

}
