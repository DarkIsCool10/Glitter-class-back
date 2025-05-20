package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.CrearExamenDto;
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
            examenService.crearExamen(dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Examen creado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(),null));
        }
    }

}

