package co.edu.uniquindio.proyectobases.controller;

import java.util.List;
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
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ObtenerExamenDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ExamenGrupoDto;
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
    public ResponseEntity<MensajeDto<Long>> crear(@RequestBody CrearExamenDto dto) throws ExamenException {
        try {
            Optional<Long> idExamen = examenService.crearExamen(dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Examen creado exitosamente", idExamen.get()));
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

}

