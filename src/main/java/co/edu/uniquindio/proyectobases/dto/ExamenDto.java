package co.edu.uniquindio.proyectobases.dto;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ExamenDto(
    Long idGrupo,
    Long idDocente,
    Long idTema,
    String titulo,
    String descripcion,
    Integer cantidadPreguntas,
    Integer preguntasMostradas,
    Integer tiempoLimite,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaDisponible,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaCierre,
    Double pesoEnCurso,
    Double umbralAprobacion,
    Integer aleatorizarPreguntas,
    Integer mostrarResultados,
    String modo,
    Long idUnidad,
    Long idEstado
) {}
