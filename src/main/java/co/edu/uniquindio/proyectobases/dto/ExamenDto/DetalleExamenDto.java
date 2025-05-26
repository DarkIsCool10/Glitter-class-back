package co.edu.uniquindio.proyectobases.dto.ExamenDto;

import java.util.List;

import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaOpcionesExamenDto;

public record DetalleExamenDto(
    Long idExamen,
    String titulo,
    String descripcion,
    Long idTema,
    String nombreTema,
    Integer tiempoLimite,
    Double pesoEnCurso,
    List<PreguntaOpcionesExamenDto> preguntas
) {}
