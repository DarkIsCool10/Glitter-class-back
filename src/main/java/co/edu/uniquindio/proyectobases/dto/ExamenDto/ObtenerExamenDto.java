package co.edu.uniquindio.proyectobases.dto.ExamenDto;

import java.sql.Timestamp;

public record ObtenerExamenDto(
    Long idExamen,
    Long idTema,
    String tema,
    String titulo,
    String descripcion,
    Integer cantidadPreguntas,
    Integer preguntasMostradas,
    Integer tiempoLimite,
    Timestamp fechaDisponible,
    Timestamp fechaCierre,
    Double pesoEnCurso,
    Double umbralAprobacion,
    Long idUnidad,
    String unidadAcademica,
    String estado
) {}
