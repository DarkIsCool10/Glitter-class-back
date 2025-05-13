package co.edu.uniquindio.proyectobases.dto;

public record EstudianteCursoDto(
    Long idEstudiante,
    String nombreEstudiante,
    Long idCurso,
    String nombreCurso,
    Integer creditos,
    String unidadAcademica,
    Long idGrupo,
    String nombreGrupo
) {}
