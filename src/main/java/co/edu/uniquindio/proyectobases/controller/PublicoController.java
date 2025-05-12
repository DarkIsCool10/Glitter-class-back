package co.edu.uniquindio.proyectobases.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.CategoriaDto;
import co.edu.uniquindio.proyectobases.dto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.TipoPreguntaDto;
import co.edu.uniquindio.proyectobases.service.PublicoService;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class PublicoController {

    private final PublicoService publicoService;

    public PublicoController(PublicoService publicoService) {
        this.publicoService = publicoService;
    }

    @GetMapping("/obtener-usuario/{id}")
    public ResponseEntity<MensajeDto<String>> obtenerUsuario(@PathVariable Long id) {
       try{
        publicoService.obtenerUsuario(id);
        return ResponseEntity.ok(new MensajeDto<>(false, "Usuario encontrado"));
       }catch(Exception e){
        return ResponseEntity.status(404).body(new MensajeDto<>(true, "Usuario no encontrado"));
       }
    }
    
    @GetMapping("/obtener-categorias")
    public ResponseEntity<MensajeDto<List<CategoriaDto>>> listar(){
        return ResponseEntity.ok(publicoService.obtenerCategorias());
    } 

    @GetMapping("/obtener-cursos")
    public ResponseEntity<MensajeDto<List<CursoDto>>> listarCursos() {
        return ResponseEntity.ok(publicoService.obtenerCursos());
    }

    @GetMapping("/obtener-dificultades")
    public ResponseEntity<MensajeDto<List<DificultadDto>>> listarDificultades() {
        return ResponseEntity.ok(publicoService.obtenerDificultades());
    }

    @GetMapping("/obtener-tipos")
    public ResponseEntity<MensajeDto<List<TipoPreguntaDto>>> listarTipos() {
        return ResponseEntity.ok(publicoService.obtenerTipos());
    }

}
