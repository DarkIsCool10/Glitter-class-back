package co.edu.uniquindio.proyectobases.dto.ExamenDto;

import java.util.List;

import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaEstudianteDto;

public record ExamenEstudianteDto(
    Long idExamenEstudiante,
    Long idExamen,
    Long idEstudiante,
    List<PreguntaEstudianteDto> preguntas
) {}
