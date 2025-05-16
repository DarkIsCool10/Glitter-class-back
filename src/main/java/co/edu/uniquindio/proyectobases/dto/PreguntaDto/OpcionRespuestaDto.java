package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

public record OpcionRespuestaDto(
    Long idPregunta,
    String textoOpcion,
    Double porcentajeParcial,
    Integer orden,
    Long idTipoRespuesta
) {}
