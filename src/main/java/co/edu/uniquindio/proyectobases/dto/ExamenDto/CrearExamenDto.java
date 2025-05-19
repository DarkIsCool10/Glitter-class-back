package co.edu.uniquindio.proyectobases.dto.ExamenDto;

import java.sql.Timestamp;
import java.util.List;

public record CrearExamenDto(
    Long idGrupo,
    Long idDocente,
    Long idTema,
    String titulo,
    String descripcion,
    Integer preguntasMostradas,
    Integer tiempoLimite,
    Timestamp fechaDisponible,
    Timestamp fechaCierre,
    Double pesoEnCurso,
    Double umbralAprobacion,
    Integer aleatorizarPreguntas,
    Integer mostrarResultados,
    Long idUnidad,
    Long idEstado,
    List<PreguntaExamenDto> listaPreguntas
) {}
