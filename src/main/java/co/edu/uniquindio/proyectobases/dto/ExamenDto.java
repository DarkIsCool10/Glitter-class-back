package co.edu.uniquindio.proyectobases.dto;

import java.security.Timestamp;

public record ExamenDto(
    Long idGrupo,
    Long idDocente,
    Long idTema,
    String titulo,
    String descripcion,
    Integer cantidadPreguntas,
    Integer preguntasMostradas,
    Integer tiempoLimite,
    Timestamp fechaDisponible,
    Timestamp fechaCierre,
    Double pesoEnCurso,
    Double umbralAprobacion,
    Integer aleatorizarPreguntas,
    Integer mostrarResultados,
    String modo,
    Long idUnidad,
    Long idEstado
) {}
