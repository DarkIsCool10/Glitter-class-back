package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

public record PreguntaPublicaDto(
    Long idPregunta,
    String enunciado,
    String tema,
    String visibilidad,
    String dificultad,
    String docente,
    String unidadAcademica,
    String tipoPregunta
) {}

