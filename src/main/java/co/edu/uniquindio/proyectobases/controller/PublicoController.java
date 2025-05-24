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
import co.edu.uniquindio.proyectobases.dto.PublicoDto.ObtenerGruposDocenteDto;
import co.edu.uniquindio.proyectobases.dto.UsuarioDto.UsuarioDetalleDto;
import co.edu.uniquindio.proyectobases.exception.PublicoException;
import co.edu.uniquindio.proyectobases.service.PublicoService;


/**
 * Controlador que gestiona las operaciones publicas.
 */
@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class PublicoController {

    /**
     * Servicio que gestiona las operaciones publicas.
     */
    private final PublicoService publicoService;

    /**
     * Constructor que inicializa el servicio.
     * @param publicoService servicio que gestiona las operaciones publicas
     */
    public PublicoController(PublicoService publicoService) {
        this.publicoService = publicoService;
    }

    /**
     * Obtiene un usuario por su id.
     * @param id identificador del usuario
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener el usuario
     */
    @GetMapping("/obtener-usuario/{id}")
    public ResponseEntity<MensajeDto<UsuarioDetalleDto>> obtenerUsuario(@PathVariable Long id) throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Usuario obtenido exitosamente", publicoService.obtenerUsuario(id)));
    }

    /**
     * Obtiene todos los temas.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener los temas
     */
    @GetMapping("/obtener-temas")
    public ResponseEntity<MensajeDto<List<TemaDto>>> listar() throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Temas obtenidos exitosamente", publicoService.obtenerTemas()));
    } 

    /**
     * Obtiene todas las dificultades.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener las dificultades
     */
    @GetMapping("/obtener-dificultades")
    public ResponseEntity<MensajeDto<List<DificultadDto>>> listarDificultades() throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Dificultades obtenidas exitosamente", publicoService.obtenerDificultades()));
    }

    /**
     * Obtiene todos los tipos de preguntas.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener los tipos de preguntas
     */
    @GetMapping("/obtener-tipos")
    public ResponseEntity<MensajeDto<List<TipoPreguntaDto>>> listarTipos() throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Tipos obtenidos exitosamente", publicoService.obtenerTipos()));
    }

    /**
     * Obtiene todos los cursos dictados por un docente.
     * @param id identificador del docente
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener los cursos
     */
    @GetMapping("/cursos-docente/{id}")
    public ResponseEntity<MensajeDto<List<CursoDto>>> cursosPorDocente(@PathVariable("id") Long id) throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Cursos obtenidos exitosamente", publicoService.obtenerCursosDocente(id)));
    }

    /**
     * Obtiene todos los cursos inscritos por un estudiante.
     * @param id identificador del estudiante
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener los cursos
     */
    @GetMapping("/cursos-estudiante/{id}")
    public ResponseEntity<MensajeDto<List<CursoDto>>> cursosPorEstudiante(@PathVariable("id") Long id) throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Cursos obtenidos exitosamente", publicoService.obtenerCursosEstudiante(id)));
    }

    /**
     * Obtiene todas las preguntas publicas.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener las preguntas
     */
    @GetMapping("/obtener-preguntas-publicas")
    public ResponseEntity<MensajeDto<List<ObtenerPreguntaDto>>> listarPreguntasPublicas() throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Preguntas obtenidas exitosamente", publicoService.obtenerPreguntasPublicas()));
    }

    /**
     * Obtiene todas las visibilidades.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener las visibilidades
     */
    @GetMapping("/obtener-visibilidades")
    public ResponseEntity<MensajeDto<List<VisibilidadDto>>> listarVisibilidades() throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Visibilidades obtenidas exitosamente", publicoService.obtenerVisibilidades()));
    }

    /**
     * Obtiene todos los examenes.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener los examenes
     */
    @GetMapping("/obtener-examenes")
    public ResponseEntity<MensajeDto<List<ExamenResumenDto>>> listarExamenes() throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Examenes obtenidos exitosamente", publicoService.obtenerExamenes()));
    }

    /**
     * Obtiene todas las unidades académicas.
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener las unidades académicas
     */
    @GetMapping("/obtener-unidades")
    public ResponseEntity<MensajeDto<List<UnidadAcademicaDto>>> listarUnidades() throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Unidades obtenidas exitosamente", publicoService.obtenerUnidades()));
    }

    /**
     * Obtiene todas las unidades académicas de un docente.
     * @param id identificador del docente
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener las unidades académicas
     */
    @GetMapping("/obtener-unidades-docente/{id}")
    public ResponseEntity<MensajeDto<List<UnidadAcademicaDto>>> listarUnidadesDocente(@PathVariable("id") Long id) throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Unidades obtenidas exitosamente", publicoService.obtenerUnidadesDocente(id)));
    }

    /**
     * Obtiene todos los temas de una unidad académica.
     * @param id identificador de la unidad académica
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener los temas
     */
    @GetMapping("/obtener-temas-unidad/{id}")
    public ResponseEntity<MensajeDto<List<TemaDto>>> listarTemasUnidad(@PathVariable("id") Long id) throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Temas obtenidos exitosamente", publicoService.obtenerTemasUnidad(id)));
    }

    /**
     * Obtiene todos los grupos de un docente.
     * @param idDocente identificador del docente
     * @return ResponseEntity con el mensaje de respuesta
     * @throws PublicoException si ocurre un error al obtener los grupos
     */
    @GetMapping("/obtener-grupos-docente/{idDocente}")
    public ResponseEntity<MensajeDto<List<ObtenerGruposDocenteDto>>> listarGruposDocente(@PathVariable("idDocente") Long idDocente) throws PublicoException {
        return ResponseEntity.ok(new MensajeDto<>(false, "Grupos obtenidos exitosamente", publicoService.obtenerGruposDocente(idDocente)));
    }

}
