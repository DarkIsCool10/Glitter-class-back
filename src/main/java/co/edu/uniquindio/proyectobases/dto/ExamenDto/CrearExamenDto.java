package co.edu.uniquindio.proyectobases.dto.ExamenDto;

import java.sql.Timestamp;

public record CrearExamenDto(
    Long idGrupo,
    Long idDocente,
    Long idTema,
    String titulo,
    String descripcion,
    // Integer cantidadPreguntas, -- Se setean nulos al momento de crear el examen
    // Integer preguntasMostradas,
    Integer tiempoLimite,
    Timestamp fechaDisponible,
    Timestamp fechaCierre,
    Double pesoEnCurso,
    Double umbralAprobacion,
    Long idUnidad,
    Long idEstado
) {}
