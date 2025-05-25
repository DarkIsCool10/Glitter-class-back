package co.edu.uniquindio.proyectobases.service;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.ExamenRepository;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.CrearExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ObtenerExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.RespuestaCrearExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.cantidadPreguntasDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ExamenGrupoDto;
import co.edu.uniquindio.proyectobases.exception.ExamenException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio relacionada con exámenes.
 * Permite crear exámenes a través del repositorio y retorna los resultados en objetos MensajeDto.
 */
@Service
public class ExamenService {

    /**
     * Repositorio que gestiona la persistencia y consulta de exámenes en la base de datos.
     */
    private final ExamenRepository examenRepository;

    /**
     * Constructor con inyección de dependencias.
     * @param examenRepository repositorio de exámenes
     */
    public ExamenService(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    /**
     * Crea un nuevo examen en la base de datos utilizando los datos proporcionados en el DTO.
     * Si la creación es exitosa, retorna el id del examen creado; en caso de error, retorna null y marca el mensaje como error.
     *
     * @param dto DTO con los datos necesarios para crear el examen
     * @return Optional con el identificador del examen creado si la operación fue exitosa
     * @throws ExamenException si ocurre un error al crear el examen
     */
    public Optional<RespuestaCrearExamenDto> crearExamen(CrearExamenDto dto) throws ExamenException {
        return examenRepository.crearExamen(dto);
    }

    /**
     * Lista todos los exámenes asociados a un docente en la base de datos.
     * Si la lista es exitosa, retorna la lista de exámenes; en caso de error, retorna null y marca el mensaje como error.
     *
     * @param idDocente identificador del docente
     * @return Optional con la lista de exámenes si la operación fue exitosa
     * @throws ExamenException si ocurre un error al listar los exámenes
     */
    public List<ObtenerExamenDto> listarExamenesDocente(Long idDocente) throws ExamenException {
        return examenRepository.listarExamenesDocente(idDocente);
    }

    /**
     * Lista todos los exámenes asociados a un grupo en la base de datos.
     * Si la lista es exitosa, retorna la lista de exámenes; en caso de error, retorna null y marca el mensaje como error.
     *
     * @param idGrupo identificador del grupo
     * @return Optional con la lista de exámenes si la operación fue exitosa
     * @throws ExamenException si ocurre un error al listar los exámenes
     */
    public List<ExamenGrupoDto> listarExamenesGrupo(Long idGrupo) throws ExamenException {
        return examenRepository.ListarExamenGrupo(idGrupo);
    }

    /**
     * Agrega una pregunta a un examen en la base de datos.
     * Si la operación es exitosa, retorna el id de la pregunta agregada; en caso contrario, retorna Optional.empty().
     *
     * @param idExamen identificador del examen al que se agregará la pregunta
     * @param idPregunta identificador de la pregunta que se agregará al examen
     * @return Optional con el id de la pregunta agregada si la operación fue exitosa
     * @throws ExamenException si ocurre un error al agregar la pregunta al examen
     */
    public void agregarPreguntaAExamen(Long idExamen, Long idPregunta) throws ExamenException {
        int resultado = examenRepository.agregarPreguntaExamen(idExamen, idPregunta);
        if (resultado == -1) {
            throw new ExamenException("La unidad o el tema de la pregunta no coincide con el del examen.");
        } else if (resultado != 1) {
            throw new ExamenException("Error al agregar la pregunta al examen.");
        }
    }

    /**
     * Actualiza la cantidad de preguntas de un examen en la base de datos.
     * Si la operación es exitosa, retorna el resultado; en caso contrario, retorna null y marca el mensaje como error.
     *
     * @param dto DTO con los datos de actualización
     * @return Map con los resultados
     * @throws ExamenException si ocurre un error al actualizar la cantidad de preguntas
     */
    public Map<String, Object> actualizarCantidadPreguntas(cantidadPreguntasDto dto) throws ExamenException {
        return examenRepository.actualizarCantidadPreguntas(dto);
    }

}
