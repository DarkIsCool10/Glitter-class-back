package co.edu.uniquindio.proyectobases.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.exception.PreguntaException;
import co.edu.uniquindio.proyectobases.service.PreguntaService;

/**
 * Controlador que gestiona las operaciones relacionadas con las preguntas.
 */
@RestController
@RequestMapping("/api/pregunta")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class PreguntaController {

    /**
     * Servicio que gestiona las operaciones relacionadas con las preguntas.
     */
    @Autowired
    private PreguntaService preguntaService;
    
    /**
     * Crea una nueva pregunta.
     * @param dto DTO con los datos de la pregunta
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PreguntaException si ocurre un error al crear la pregunta
     */
    @PostMapping("/crear-pregunta")
    public ResponseEntity<MensajeDto<Long>> crearPregunta(@RequestBody PreguntaDto dto) throws PreguntaException {
        try{
            Optional<Long> idPregunta = preguntaService.crearPregunta(dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Pregunta creada exitosamente", idPregunta.get()));
        }catch(PreguntaException e){
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }
    
    /**
     * Crea una nueva opción para una pregunta.
     * @param idPregunta identificador de la pregunta
     * @param dto DTO con los datos de la opción
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PreguntaException si ocurre un error al crear la opción
     */
    @PostMapping("/crear-opcion/{id}")
    public ResponseEntity<MensajeDto<Long>> crearOpcion(@PathVariable("id") Long idPregunta,@RequestBody OpcionRespuestaDto dto) throws PreguntaException {
        try{
            Optional<Long> idOpcion = preguntaService.crearOpcion(idPregunta, dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Opcion creada exitosamente", idOpcion.get()));
        }catch(PreguntaException e){
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

    /**
     * Obtiene todas las preguntas con sus opciones.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PreguntaException si ocurre un error al obtener las preguntas
     */
    @GetMapping("/obtener-preguntas-opciones")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> obtenerTodasLasPreguntasConOpciones() throws PreguntaException {
        List<ObtenerPreguntaDto> preguntas = preguntaService.obtenerTodasLasPreguntasConOpciones();
        return ResponseEntity.ok(new MensajeDto<>(false, "Preguntas obtenidas exitosamente", preguntas));
    }

    /**
     * Obtiene todas las preguntas con sus opciones.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PreguntaException si ocurre un error al obtener las preguntas
     */
    @GetMapping("/obtener-preguntas-docente/{id}")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> listarPreguntasDocente(@PathVariable("id") Long id) throws PreguntaException {
        List<ObtenerPreguntaDto> preguntas = preguntaService.obtenerPreguntasDocente(id);
        return ResponseEntity.ok(new MensajeDto<>(false, "Preguntas obtenidas exitosamente", preguntas));
    }

    /**
     * Obtiene una lista de preguntas filtradas por el tema especificado.
     *
     * @param idTema identificador del tema por el cual se filtran las preguntas
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PreguntaException si ocurre un error al obtener las preguntas
     */
    @GetMapping("/obtener-preguntas-tema/{idTema}")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> listarPreguntasTema(@PathVariable("idTema") Long idTema) throws PreguntaException {
        List<ObtenerPreguntaDto> preguntas = preguntaService.obtenerPreguntasTema(idTema);
        return ResponseEntity.ok(new MensajeDto<>(false, "Preguntas obtenidas exitosamente", preguntas));
    }

    /**
     * Obtiene una lista de preguntas filtradas por el docente y el tema.
     *
     * @param idDocente identificador del docente
     * @param idTema identificador del tema
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PreguntaException si ocurre un error al obtener las preguntas
     */
    @GetMapping("/obtener-preguntas-docente-tema/{idDocente}/{idTema}")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> listarPreguntasDocenteYTema(@PathVariable("idDocente") Long idDocente, @PathVariable("idTema") Long idTema) throws PreguntaException {
        List<ObtenerPreguntaDto> preguntas = preguntaService.obtenerPreguntasDocenteYTema(idDocente, idTema);
        return ResponseEntity.ok(new MensajeDto<>(false, "Preguntas obtenidas exitosamente", preguntas));
    }

}
