package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenDto;
import co.edu.uniquindio.proyectobases.service.ExamenService;

@RestController
@RequestMapping("/api/examen")
public class ExamenController {

    private final ExamenService examenService;

    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @PostMapping("/crear-examen")
    public ResponseEntity<MensajeDto<Long>> crear(@RequestBody ExamenDto dto) {
        MensajeDto<Long> respuesta = examenService.crearExamen(dto);
        return ResponseEntity.status(respuesta.error() ? 400 : 201).body(respuesta);
    }
}

