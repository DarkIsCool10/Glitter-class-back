package co.edu.uniquindio.proyectobases.dto.PublicoDto;

public record ObtenerGruposDocenteDto(
    Long idGrupo,
    String nombre,
    Long idCurso,
    String nombreCurso
) {}
