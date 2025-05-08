package co.edu.uniquindio.proyectobases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto;
import co.edu.uniquindio.proyectobases.service.PreguntaService;

@RestController
@RequestMapping("/api/pregunta")

public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;
    
    @PostMapping("/crear-pregunta")
    public ResponseEntity<MensajeDto<Long>> crearPregunta(@RequestBody PreguntaDto dto) {
        MensajeDto<Long> respuesta = preguntaService.crearPregunta(dto);
        return ResponseEntity.status(respuesta.error() ? 400 : 201).body(respuesta);
    }
    

}
