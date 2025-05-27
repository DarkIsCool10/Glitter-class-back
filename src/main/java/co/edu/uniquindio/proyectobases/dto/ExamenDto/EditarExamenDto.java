package co.edu.uniquindio.proyectobases.dto.ExamenDto;

import java.sql.Timestamp;

public record EditarExamenDto(
    Long idExamen,
    String titulo,
    String descripcion,
    Integer preguntasMostradas,
    Integer tiempoLimite,
    Timestamp fechaDisponible,
    Timestamp fechaCierre,
    Double pesoEnCurso,
    Double umbralAprobacion
) {}

