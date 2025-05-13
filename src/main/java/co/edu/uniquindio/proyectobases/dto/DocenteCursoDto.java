package co.edu.uniquindio.proyectobases.dto;

public record DocenteCursoDto(
    Long idDocente,
    String nombreDocente,
    Long idCurso,
    String nombreCurso,
    Integer creditos,
    String unidadAcademica,
    Long idGrupo,
    String nombreGrupo

) {}
