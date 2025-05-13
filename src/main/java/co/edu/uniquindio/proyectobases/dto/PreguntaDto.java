package co.edu.uniquindio.proyectobases.dto;

public record PreguntaDto(
    String enunciado,
    Long idTema,
    Long idDificultad,
    Long idTipo,
    Integer tiempoMaximo,
    Double porcentajeNota,
    Long idVisibilidad,
    Long idDocente,
    Long idUnidad,
    Long idEstado
) {}
