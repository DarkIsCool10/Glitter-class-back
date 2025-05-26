package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

import java.util.List;

public record PreguntaOpcionesExamenDto(
    Long idPregunta,
    String enunciado,
    Long idTipo,
    Long idDocente,
    Long idUnidad,
    List<ObtenerOpcionRespuestaDto> opciones
) {}
