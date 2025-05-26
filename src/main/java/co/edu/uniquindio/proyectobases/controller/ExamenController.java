package co.edu.uniquindio.proyectobases.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.CrearExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenGrupoDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ObtenerExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.RespuestaCrearExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.cantidadPreguntasDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaEstudianteDto;
import co.edu.uniquindio.proyectobases.exception.ExamenException;
import co.edu.uniquindio.proyectobases.service.ExamenService;

/**
 * Controlador que gestiona las operaciones relacionadas con exámenes.
 */
@RestController
@RequestMapping("/api/examen")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class ExamenController {

    /**
     * Servicio que gestiona las operaciones relacionadas con exámenes.
     */
    private final ExamenService examenService;

    /**
     * Constructor que inicializa el servicio.
     * @param examenService servicio que gestiona las operaciones relacionadas con exámenes
     */
    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    /**
     * Crea un nuevo examen.
     * @param dto DTO con los datos del examen
     * @return ResponseEntity con el mensaje de respuesta
     * @throws ExamenException si ocurre un error al crear el examen
     */
    @PostMapping("/crear-examen")
    public ResponseEntity<MensajeDto<RespuestaCrearExamenDto>> crearExamen(@RequestBody CrearExamenDto dto) throws ExamenException {
        try {
            Optional<RespuestaCrearExamenDto> examen = examenService.crearExamen(dto);
            if (examen.isPresent()) {
                return ResponseEntity.ok(new MensajeDto<>(false, "Examen creado exitosamente", examen.get()));
            } else {
                return ResponseEntity.badRequest().body(new MensajeDto<>(true, "Error al crear el examen", null));
            }
        } catch (ExamenException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(),null));
        }
    }

    /**
     * Lista todos los exámenes asociados a un docente.
     * @param idDocente identificador del docente
     * @return ResponseEntity con la lista de exámenes
     * @throws ExamenException si ocurre un error al listar los exámenes
     */
    @GetMapping("/listar-examenes-docente/{idDocente}")
    public ResponseEntity<MensajeDto<List<ObtenerExamenDto>>> listarExamenesDocente(@PathVariable Long idDocente) throws ExamenException {
        try {
            List<ObtenerExamenDto> examenes = examenService.listarExamenesDocente(idDocente);
            return ResponseEntity.ok(new MensajeDto<>(false, "Examenes obtenidos exitosamente", examenes));
        } catch (ExamenException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

    /**
     * Lista todos los exámenes asociados a un grupo.
     * @param idGrupo identificador del grupo
     * @return ResponseEntity con la lista de exámenes
     * @throws ExamenException si ocurre un error al listar los exámenes
     */
    @GetMapping("/listar-examenes-grupo/{idGrupo}")
    public ResponseEntity<MensajeDto<List<ExamenGrupoDto>>> listarExamenesGrupo(@PathVariable Long idGrupo) throws ExamenException {
        try {
            List<ExamenGrupoDto> examenes = examenService.listarExamenesGrupo(idGrupo);
            return ResponseEntity.ok(new MensajeDto<>(false, "Examenes obtenidos exitosamente", examenes));
        } catch (ExamenException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

    /**
     * Agrega una pregunta a un examen.
     * @param idExamen identificador del examen
     * @param idPregunta identificador de la pregunta
     * @return ResponseEntity con el mensaje de respuesta
     * @throws ExamenException si ocurre un error al agregar la pregunta al examen
     */
    @PostMapping("/agregar-pregunta-examen/{idExamen}/{idPregunta}")
    public ResponseEntity<MensajeDto<Long>> agregarPreguntaExamen(@PathVariable Long idExamen, @PathVariable Long idPregunta) throws ExamenException {
        try {
            examenService.agregarPreguntaAExamen(idExamen, idPregunta);
            return ResponseEntity.ok(new MensajeDto<>(false, "Pregunta agregada exitosamente", null));
        } catch (ExamenException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

    /**
     * Actualiza la cantidad de preguntas de un examen.
     * @param dto DTO con los datos de actualización
     * @return ResponseEntity con el mensaje de respuesta
     * @throws ExamenException si ocurre un error al actualizar la cantidad de preguntas
     */
    @PostMapping("/actualizar-cantidad-preguntas")
    public ResponseEntity<MensajeDto<Map<String, Object>>> actualizarCantidadPreguntas(@RequestBody cantidadPreguntasDto dto) throws ExamenException {
        try {
            Map<String, Object> result = examenService.actualizarCantidadPreguntas(dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Cantidad de preguntas actualizada exitosamente", result));
        } catch (ExamenException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

    /**
     * Genera un examen para un estudiante.
     * @param idExamen identificador del examen
     * @param idEstudiante identificador del estudiante
     * @return ResponseEntity con el mensaje de respuesta
     * @throws ExamenException si ocurre un error al generar el examen
     */
    @PostMapping("/generar-examen-estudiante/{idExamen}/{idEstudiante}")
    public ResponseEntity<MensajeDto<Integer>> generarExamenEstudiante(@PathVariable Long idExamen, @PathVariable Long idEstudiante) throws ExamenException {
        try {
            int result = examenService.generarExamenEstudiante(idExamen, idEstudiante);
            return ResponseEntity.ok(new MensajeDto<>(false, "Examen generado exitosamente", result));
        } catch (ExamenException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

    /**
     * Obtiene un examen para un estudiante
     * @param idExamen identificador del examen
     * @param idEstudiante identificador del estudiante
     * @return ResponseEntity con el mensaje de respuesta
     * @throws ExamenException si ocurre un error al obtener el examen
     */
    @GetMapping("/obtener-examen-estudiante/{idExamen}/{idEstudiante}")
    public ResponseEntity<MensajeDto<List<PreguntaEstudianteDto>>> obtenerExamenEstudiante(@PathVariable Long idExamen, @PathVariable Long idEstudiante) throws ExamenException {
        try {
            List<PreguntaEstudianteDto> examen = examenService.obtenerExamenEstudiante(idExamen, idEstudiante);
            return ResponseEntity.ok(new MensajeDto<>(false, "Examen obtenido exitosamente", examen));
        } catch (ExamenException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

}

