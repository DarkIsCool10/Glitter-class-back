package co.edu.uniquindio.proyectobases.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Table(name = "PREGUNTA")
public class Pregunta {

    @Id
    @GeneratedValue
    @Column(name = "IDPREGUNTA")
    private Long idPregunta;

    @Column(name = "ENUNCIADO", columnDefinition = "CLOB")
    private String enunciado;

    @Column(name = "IDTEMA")
    private Long idTema;

    @Column(name = "IDCATEGORIA")
    private Long idCategoria;

    @Column(name = "IDDIFICULTAD")
    private Long idDificultad;

    @Column(name = "IDTIPO")
    private Long idTipo;

    @Column(name = "TIEMPOMAXIMO")
    private Integer tiempoMaximo;

    @Column(name = "PORCENTAJENOTA")
    private Double porcentajeNota;

    @Column(name = "IDVISIBILIDAD")
    private Long idVisibilidad;    

    @Column(name = "IDDOCENTE")
    private Long idDocente;

    @Column(name = "IDUNIDAD")
    private Long idUnidad;

    @Column(name = "FECHACREACION")
    private Timestamp fechaCreacion;

    @Column(name = "ESTADO")
    private String estado;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = Timestamp.valueOf(LocalDateTime.now());
    }
}
