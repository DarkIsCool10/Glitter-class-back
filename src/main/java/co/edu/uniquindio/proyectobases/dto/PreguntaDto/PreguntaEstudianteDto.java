package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

import java.util.List;

public record PreguntaEstudianteDto(
    Long idPregunta,
    String enunciado,
    Long idTipo,
    Long idDocente,
    Long idUnidad,
    Double porcentajePregunta,
    List<ObtenerOpcionRespuestaDto> opciones
) {}
