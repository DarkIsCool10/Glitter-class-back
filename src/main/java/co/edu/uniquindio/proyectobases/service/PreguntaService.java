package co.edu.uniquindio.proyectobases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaConOpcionesDto;
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
     * Crea una nueva pregunta en la base de datos a partir de los datos proporcionados en el DTO.
     * Si la creación es exitosa, retorna el id de la pregunta creada, de lo contrario retorna null.
     *
     * @param dto DTO con los datos de la pregunta a crear
     * @return Optional<Long> con el id de la pregunta creada o null si hubo error
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
     * Si la creación es exitosa, retorna el DTO con la información de la opción creada.
     *
     * @param idPregunta id de la pregunta a la que se asocia la opción
     * @param dto DTO con los datos de la opción de respuesta
     * @return OpcionRespuestaDto con el DTO de la opción creada o null si hubo error
     */
    public Optional<Long> crearOpcion(Long idPregunta, OpcionRespuestaDto dto) throws PreguntaException {
        Optional<Long> idOpcion = preguntaRepository.crearOpcionRespuesta(idPregunta, dto);
        if (idOpcion.isPresent()) {
            return idOpcion;
        } else {
            throw new PreguntaException("Error al crear la opcion de respuesta");
        }
    }

    /**
     * Obtiene todas las preguntas junto con sus opciones de respuesta asociadas.
     *
     * @return lista de preguntas con sus opciones de respuesta
     */
    public List<PreguntaConOpcionesDto> obtenerTodasLasPreguntasConOpciones() {
        return preguntaRepository.obtenerTodasLasPreguntasConOpciones();
    }

    /**
     * Obtiene todas las preguntas creadas por un docente específico, incluyendo detalles relevantes.
     *
     * @param idUsuario id del docente
     * @return lista de preguntas del docente
     */
    public List<ObtenerPreguntaDto> obtenerPreguntasDocente(Long idUsuario) {
        return preguntaRepository.listarPreguntasDocente(idUsuario);
    }
}

