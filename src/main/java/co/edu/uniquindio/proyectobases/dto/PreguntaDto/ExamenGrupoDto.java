package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

import java.sql.Timestamp;

public record ExamenGrupoDto(
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
