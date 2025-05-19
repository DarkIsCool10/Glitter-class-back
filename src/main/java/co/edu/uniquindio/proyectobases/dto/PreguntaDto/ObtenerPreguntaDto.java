package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

import java.time.LocalDateTime;

public record ObtenerPreguntaDto(
    Long idPregunta,
    String enunciado,
    String tema,
    String visibilidad,
    String dificultad,
    String docente,
    String unidadAcademica,
    String tipo,
    Double porcentajeNota,
    LocalDateTime fechaCreacion,
    String estado
) {}

