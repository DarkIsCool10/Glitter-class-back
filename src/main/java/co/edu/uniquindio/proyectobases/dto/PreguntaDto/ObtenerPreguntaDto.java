package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

import java.sql.Timestamp;
import java.util.List;

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
    Timestamp fechaCreacion,
    String estado,
    List<ObtenerOpcionRespuestaDto> opciones
) {}

