package co.edu.uniquindio.proyectobases.controller;

import java.util.List;

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
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaConOpcionesDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.service.PreguntaService;

@RestController
@RequestMapping("/api/pregunta")
@CrossOrigin(origins = {"http://localhost:4200", "*"})

public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;
    
    @PostMapping("/crear-pregunta")
    public ResponseEntity<MensajeDto<Long>> crearPregunta(@RequestBody PreguntaDto dto) {
        try{
            MensajeDto<Long> respuesta = preguntaService.crearPregunta(dto);
            return ResponseEntity.ok(respuesta);
        }catch(Exception e){
            return ResponseEntity.status(400).body(new MensajeDto<>(true, null));
        }
    }
    
    @PostMapping("/crear-opcion")
    public ResponseEntity<MensajeDto<String>> crearOpcion(@RequestBody OpcionRespuestaDto dto) {
        try{
            preguntaService.crearOpcion(dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Opcion creada exitosamente"));
        }catch(Exception e){
            return ResponseEntity.status(400).body(new MensajeDto<>(true, "Error al crear la opcion"));
        }
    }

    @GetMapping("/obtener-preguntas-opciones")
    public ResponseEntity<MensajeDto<List<PreguntaConOpcionesDto>>> obtenerTodasLasPreguntasConOpciones() {
        try {
            List<PreguntaConOpcionesDto> preguntas = preguntaService.obtenerTodasLasPreguntasConOpciones();
            return ResponseEntity.ok(new MensajeDto<>(false, preguntas));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new MensajeDto<>(true, null));
        }
    }
 
    @GetMapping("/obtener-preguntas-docente/{id}")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> listarPreguntasDocente(@PathVariable("id") Long id) {
        return ResponseEntity.ok(preguntaService.obtenerPreguntasDocente(id));
    }
}
