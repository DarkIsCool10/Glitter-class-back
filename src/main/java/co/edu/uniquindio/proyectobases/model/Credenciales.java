package co.edu.uniquindio.proyectobases.model;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "CREDENCIALESACCESO")
public class Credenciales {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCREDENCIALES")
    private Long idCredenciales;

    @Column(name = "IDUSUARIO")
    private Long idUsuario;

    @Column(name = "CORREO")
    private String correo;

    @Column(name = "CONTRASENA")
    private String contrasena;

    @Column(name = "IDROL")
    private Long idRol;
}
