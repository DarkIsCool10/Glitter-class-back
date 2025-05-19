package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.CrearExamenDto;
import co.edu.uniquindio.proyectobases.service.ExamenService;

@RestController
@RequestMapping("/api/examen")
public class ExamenController {

    private final ExamenService examenService;

    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @PostMapping("/crear-examen")
    public ResponseEntity<MensajeDto<Long>> crear(@RequestBody CrearExamenDto dto) {
        try {
            MensajeDto<Long> respuesta = examenService.crearExamen(dto);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new MensajeDto<>(true, null));
        }
    }

}

