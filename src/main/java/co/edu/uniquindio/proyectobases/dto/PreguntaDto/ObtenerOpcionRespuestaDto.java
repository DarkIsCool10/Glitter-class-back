package co.edu.uniquindio.proyectobases.dto.PreguntaDto;

public record ObtenerOpcionRespuestaDto(
    Long idOpcionRespuesta,
    String textoOpcion,
    String textoPareja,
    Long idTipoRespuesta
) {}
