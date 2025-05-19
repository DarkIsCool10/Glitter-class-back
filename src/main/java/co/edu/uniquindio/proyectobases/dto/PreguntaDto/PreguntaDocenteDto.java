package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

import java.time.LocalDateTime;

public record PreguntaDocenteDto(
    Long idPregunta,
    String enunciado,
    String tema,
    String dificultad,
    String tipo,
    String visibilidad,
    Double porcentajeNota,
    LocalDateTime fechaCreacion,
    String estado
) {}
