package co.edu.uniquindio.proyectobases.dto.ExamenDto;

public record ExamenResumenDto(
    Long idExamen,
    String grupo,
    String docente,
    String tema,
    String titulo,
    String descripcion,
    Integer cantidadPreguntas,
    Integer tiempoLimite,
    String fechaDisponible,
    String fechaCierre,
    String unidadAcademica
) {}

