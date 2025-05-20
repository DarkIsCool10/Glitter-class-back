package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

public record OpcionRespuestaDto(
    Long idOpcionRespuesta,
    String textoOpcion,
    Integer orden,
    Long idTipoRespuesta
) {}
