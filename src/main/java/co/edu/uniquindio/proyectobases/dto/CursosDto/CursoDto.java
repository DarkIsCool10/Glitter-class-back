package co.edu.uniquindio.proyectobases.dto.CursosDto;

public record CursoDto(
    Long idUsuario,
    Long idCurso,
    String nombreCurso,
    Integer creditos,
    String unidadAcademica,
    Long idGrupo,
    String nombreGrupo,
    String nombreDocente    
) {}
