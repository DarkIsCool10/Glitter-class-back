package co.edu.uniquindio.proyectobases.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaConOpcionesDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDocenteDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.service.PreguntaService;

@RestController
@RequestMapping("/api/pregunta")

public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;
    
    @PostMapping("/crear-pregunta")
    public ResponseEntity<MensajeDto<String>> crearPregunta(@RequestBody PreguntaDto dto) {
        try{
            preguntaService.crearPregunta(dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Pregunta creada exitosamente"));
        }catch(Exception e){
            return ResponseEntity.status(400).body(new MensajeDto<>(true, "Error al crear la pregunta"));
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

    @GetMapping("/obtener-todas-las-preguntas-con-opciones")
    public ResponseEntity<MensajeDto<List<PreguntaConOpcionesDto>>> obtenerTodasLasPreguntasConOpciones() {
        try {
            List<PreguntaConOpcionesDto> preguntas = preguntaService.obtenerTodasLasPreguntasConOpciones();
            return ResponseEntity.ok(new MensajeDto<>(false, preguntas));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new MensajeDto<>(true, null));
        }
    }
 
    @GetMapping("/obtener-preguntas-docente/{id}")
    public ResponseEntity<MensajeDto<List<PreguntaDocenteDto>>> listarPreguntasDocente(@PathVariable("id") Long id) {
        return ResponseEntity.ok(preguntaService.obtenerPreguntasDocente(id));
    }
}
