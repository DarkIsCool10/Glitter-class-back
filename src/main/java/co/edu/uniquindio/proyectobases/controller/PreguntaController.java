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
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaConOpcionesDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.exception.PreguntaException;
import co.edu.uniquindio.proyectobases.service.PreguntaService;

@RestController
@RequestMapping("/api/pregunta")
@CrossOrigin(origins = {"http://localhost:4200", "*"})

public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;
    
    @PostMapping("/crear-pregunta") 
    public ResponseEntity<MensajeDto<Long>> crearPregunta(@RequestBody PreguntaDto dto) throws PreguntaException {
        try{
            preguntaService.crearPregunta(dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Pregunta creada exitosamente", null));
        }catch(PreguntaException e){
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }
    
    @PostMapping("/crear-opcion/{id}")
    public ResponseEntity<MensajeDto<Long>> crearOpcion(@PathVariable("id") Long idPregunta,@RequestBody OpcionRespuestaDto dto) throws PreguntaException {
        try{
            Optional<Long> idOpcion = preguntaService.crearOpcion(idPregunta, dto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Opcion creada exitosamente", idOpcion.get()));
        }catch(PreguntaException e){
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }

    @GetMapping("/obtener-preguntas-opciones")
    public ResponseEntity<MensajeDto<List<PreguntaConOpcionesDto>>> obtenerTodasLasPreguntasConOpciones() throws PreguntaException {
        List<PreguntaConOpcionesDto> preguntas = preguntaService.obtenerTodasLasPreguntasConOpciones();
        return ResponseEntity.ok(new MensajeDto<>(false, "Preguntas obtenidas exitosamente", preguntas));
    }
 
    @GetMapping("/obtener-preguntas-docente/{id}")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> listarPreguntasDocente(@PathVariable("id") Long id) throws PreguntaException {
        List<ObtenerPreguntaDto> preguntas = preguntaService.obtenerPreguntasDocente(id);
        return ResponseEntity.ok(new MensajeDto<>(false, "Preguntas obtenidas exitosamente", preguntas));
    }
}
