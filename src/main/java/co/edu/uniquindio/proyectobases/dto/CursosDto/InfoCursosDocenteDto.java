package co.edu.uniquindio.proyectobases.dto.CursosDto;

public record InfoCursosDocenteDto(
    Long idUsuario,
    Long idCurso,
    String nombreCurso,
    Long idGrupo,
    String nombreGrupo
) {}
