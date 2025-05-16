package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

import java.util.List;

public record PreguntaConOpcionesDto(
    Long idPregunta,
    String enunciado,
    Long idDocente,
    Long idUnidad,
    Long idVisibilidad,
    List<OpcionRespuestaDto> opciones
) {}
