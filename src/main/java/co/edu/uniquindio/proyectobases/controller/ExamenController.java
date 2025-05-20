package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.CrearExamenDto;
import co.edu.uniquindio.proyectobases.exception.ExamenException;
import co.edu.uniquindio.proyectobases.service.ExamenService;

@RestController
@RequestMapping("/api/examen")
public class ExamenController {

    private final ExamenService examenService;

    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

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

