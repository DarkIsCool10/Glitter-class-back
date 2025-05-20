package co.edu.uniquindio.proyectobases.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.CursosDto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenResumenDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TemaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TipoPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.UnidadAcademicaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.VisibilidadDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.UsuarioDto.UsuarioDetalleDto;
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
    public ResponseEntity<MensajeDto<UsuarioDetalleDto>> obtenerUsuario(@PathVariable Long id) {
        try{
            MensajeDto<UsuarioDetalleDto> usuario = publicoService.obtenerUsuario(id);
            return ResponseEntity.ok(usuario);
        }catch(Exception e){
            return ResponseEntity.status(404).body(new MensajeDto<>(true, null));
        }
    }

    @GetMapping("/obtener-temas")
    public ResponseEntity<MensajeDto<List<TemaDto>>> listar(){
        return ResponseEntity.ok(publicoService.obtenerTemas());
    } 

    // @GetMapping("/obtener-cursos")
    // public ResponseEntity<MensajeDto<List<CursoDto>>> listarCursos() {
    //     return ResponseEntity.ok(publicoService.obtenerCursos());
    // }

    @GetMapping("/obtener-dificultades")
    public ResponseEntity<MensajeDto<List<DificultadDto>>> listarDificultades() {
        return ResponseEntity.ok(publicoService.obtenerDificultades());
    }

    @GetMapping("/obtener-tipos")
    public ResponseEntity<MensajeDto<List<TipoPreguntaDto>>> listarTipos() {
        return ResponseEntity.ok(publicoService.obtenerTipos());
    }

    @GetMapping("/cursos-docente/{id}")
    public ResponseEntity<MensajeDto<List<CursoDto>>> cursosPorDocente(@PathVariable("id") Long id) {
        return ResponseEntity.ok(publicoService.obtenerCursosDocente(id));
    }

    @GetMapping("/cursos-estudiante/{id}")
    public ResponseEntity<MensajeDto<List<CursoDto>>> cursosPorEstudiante(@PathVariable("id") Long id) {
        return ResponseEntity.ok(publicoService.obtenerCursosEstudiante(id));
    }

    @GetMapping("/obtener-preguntas-publicas")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> listarPreguntasPublicas() {
        return ResponseEntity.ok(publicoService.obtenerPreguntasPublicas());
    }

    @GetMapping("/obtener-visibilidades")
    public ResponseEntity<MensajeDto<List<VisibilidadDto>>> listarVisibilidades() {
        return ResponseEntity.ok(publicoService.obtenerVisibilidades());
    }

    @GetMapping("/obtener-examenes")
    public ResponseEntity<MensajeDto<List<ExamenResumenDto>>> listarExamenes() {
        return ResponseEntity.ok(publicoService.obtenerExamenes());
    }

    @GetMapping("/obtener-unidades")
    public ResponseEntity<MensajeDto<List<UnidadAcademicaDto>>> listarUnidades() {
        return ResponseEntity.ok(publicoService.obtenerUnidades());
    }

    @GetMapping("/obtener-unidades-docente/{id}")
    public ResponseEntity<MensajeDto<List<UnidadAcademicaDto>>> listarUnidadesDocente(@PathVariable("id") Long id) {
        return ResponseEntity.ok(publicoService.obtenerUnidadesDocente(id));
    }

    @GetMapping("/obtener-temas-unidad/{id}")
    public ResponseEntity<MensajeDto<List<TemaDto>>> listarTemasUnidad(@PathVariable("id") Long id) {
        return ResponseEntity.ok(publicoService.obtenerTemasUnidad(id));
    }

}
