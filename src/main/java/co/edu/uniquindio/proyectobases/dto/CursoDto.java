package co.edu.uniquindio.proyectobases.dto;

public record CursoDto(
    Long idCurso,
    String nombre,
    String descripcion,
    Integer creditos,
    String unidadAcademica
) {}
