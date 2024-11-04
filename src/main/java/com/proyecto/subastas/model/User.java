package com.proyecto.subastas.model;

//import javax.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "correo_electronico", length = 100)
    private String correoElectronico;

    @Column(name = "contrasena", length = 100)
    private String contrasena;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "genero", length = 10)
    private String genero;

    @Column(name = "calificacion_total")
    private Double calificacionTotal;

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public double getCalificacionTotal() {
        return calificacionTotal;
    }

    public void setCalificacionTotal(double calificacionTotal) {
        this.calificacionTotal = calificacionTotal;
    }

    public String getPassword() {
        return contrasena;
    }
    
    public void setPassword(String password) {
        this.contrasena = password;
    }
}
