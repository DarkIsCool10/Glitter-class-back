package co.edu.uniquindio.proyectobases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.exception.PreguntaException;
import co.edu.uniquindio.proyectobases.repository.PreguntaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con preguntas y sus opciones de respuesta.
 * Interactúa con el repositorio de preguntas para crear preguntas, opciones y consultar información relevante,
 * retornando los resultados en objetos MensajeDto para ser usados por los controladores.
 */
@Service
public class PreguntaService {

    /**
     * Repositorio que provee acceso a las operaciones de persistencia y consulta de preguntas y opciones.
     */
    @Autowired
    private PreguntaRepository preguntaRepository;

    /**
     * Crea una nueva pregunta en la base de datos con la información proporcionada.
     *
     * @param dto DTO que contiene los datos necesarios para crear la pregunta
     * @return Optional con el identificador de la pregunta creada si la operación fue exitosa
     * @throws PreguntaException si ocurre un error al crear la pregunta
     */
    public Optional<Long> crearPregunta(PreguntaDto dto) throws PreguntaException {
        Optional<Long> idPregunta = preguntaRepository.crearPregunta(dto);
        if (idPregunta.isPresent()) {
            return idPregunta;
        } else {
            throw new PreguntaException("Error al crear la pregunta");
        }
    }

    /**
     * Crea una nueva opción de respuesta asociada a una pregunta existente.
     *
     * @param idPregunta identificador de la pregunta a la que se asociará la opción
     * @param dto DTO con los datos de la opción de respuesta a crear
     * @return Optional con el identificador de la opción creada si la operación fue exitosa
     * @throws PreguntaException si ocurre un error al crear la opción de respuesta
     */
    public Optional<Long> crearOpcion(Long idPregunta, OpcionRespuestaDto dto) throws PreguntaException {
        Optional<Long> idOpcion = preguntaRepository.crearOpcionRespuesta(idPregunta, dto);
        if (idOpcion.isPresent()) {
            return idOpcion;
        } else {
            throw new PreguntaException("Error al crear la opción de respuesta");
        }
    }

    /**
     * Obtiene todas las preguntas del sistema junto con sus opciones de respuesta asociadas.
     *
     * @return lista de DTOs que contienen preguntas con sus opciones de respuesta
     */
    public List<ObtenerPreguntaDto> obtenerTodasLasPreguntasConOpciones() {
        return preguntaRepository.obtenerTodasLasPreguntasConOpciones();
    }

    /**
     * Obtiene todas las preguntas creadas por un docente específico.
     *
     * @param idUsuario identificador del docente cuyas preguntas se desean consultar
     * @return lista de DTOs con la información de las preguntas del docente
     */
    public List<ObtenerPreguntaDto> obtenerPreguntasDocente(Long idUsuario) {
        return preguntaRepository.listarPreguntasDocente(idUsuario);
    }

    /**
     * Obtiene una lista de preguntas filtradas por el tema especificado.
     *
     * @param idTema identificador del tema por el cual se filtran las preguntas
     * @return lista de preguntas que pertenecen al tema especificado
     * @throws PreguntaException si ocurre un error al consultar las preguntas
     */
    public List<ObtenerPreguntaDto> obtenerPreguntasTema(Long idTema) throws PreguntaException {
        return preguntaRepository.listarPreguntasTema(idTema);
    }
}

