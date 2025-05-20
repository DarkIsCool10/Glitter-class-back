package co.edu.uniquindio.proyectobases.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.PublicoRepository;
import lombok.AllArgsConstructor;
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

/**
 * Servicio que expone operaciones públicas y paramétricas de la plataforma para su consumo por los controladores.
 * Encapsula la lógica de consulta y delega el acceso a datos al PublicoRepository, retornando los resultados en objetos MensajeDto.
 */
@Service
@AllArgsConstructor
public class PublicoService {

    /**
     * Repositorio que provee acceso a la información pública y paramétrica de la base de datos.
     */
    private final PublicoRepository publicoRepository;


    /**
     * Obtiene el detalle de un usuario a partir de su id.
     * @param idUsuario id del usuario a consultar
     * @return MensajeDto con el detalle del usuario si existe
     */
    public MensajeDto<UsuarioDetalleDto> obtenerUsuario(Long idUsuario) {
        Optional<UsuarioDetalleDto> usuario = publicoRepository.obtenerUsuarioPorId(idUsuario);
        return new MensajeDto<>(false, usuario.get());
    }

    /**
     * Obtiene la lista de todos los temas registrados.
     * @return MensajeDto con la lista de temas
     */
    public MensajeDto<List<TemaDto>> obtenerTemas(){
        List<TemaDto> temas = publicoRepository.listarTemas();
        return new MensajeDto<>(false, temas);
    }

    /**
     * Obtiene la lista de dificultades de preguntas disponibles.
     * @return MensajeDto con la lista de dificultades
     */
    public MensajeDto<List<DificultadDto>> obtenerDificultades() {
        List<DificultadDto> dificultad = publicoRepository.listarDificultades();
        return new MensajeDto<>(false, dificultad);
    }

    /**
     * Obtiene la lista de tipos de preguntas disponibles.
     * @return MensajeDto con la lista de tipos de pregunta
     */
    public MensajeDto<List<TipoPreguntaDto>> obtenerTipos() {
        List<TipoPreguntaDto> tipo = publicoRepository.listarTipos();
        return new MensajeDto<>(false, tipo);
    }

    /**
     * Obtiene la lista de cursos dictados por un docente.
     * @param idUsuario id del docente
     * @return MensajeDto con la lista de cursos del docente
     */
    public MensajeDto<List<CursoDto>> obtenerCursosDocente(Long idUsuario) {
        return new MensajeDto<>(false, publicoRepository.listarCursosDocente(idUsuario));
    }

    /**
     * Obtiene la lista de cursos en los que está inscrito un estudiante.
     * @param idUsuario id del estudiante
     * @return MensajeDto con la lista de cursos del estudiante
     */
    public MensajeDto<List<CursoDto>> obtenerCursosEstudiante(Long idUsuario) {
        return new MensajeDto<>(false, publicoRepository.listarCursosEstudiante(idUsuario));
    }
    
    /**
     * Obtiene la lista de todas las preguntas públicas disponibles.
     * @return MensajeDto con la lista de preguntas públicas
     */
    public MensajeDto<List<ObtenerPreguntaDto>> obtenerPreguntasPublicas() {
        return new MensajeDto<>(false, publicoRepository.listarPreguntasPublicas());
    }

    /**
     * Obtiene la lista de visibilidades posibles para las preguntas.
     * @return MensajeDto con la lista de visibilidades
     */
    public MensajeDto<List<VisibilidadDto>> obtenerVisibilidades() {
        List<VisibilidadDto> visibilidad = publicoRepository.listarVisibilidades();
        return new MensajeDto<>(false, visibilidad);
    }

    /**
     * Obtiene la lista de exámenes registrados en el sistema.
     * @return MensajeDto con la lista de exámenes
     */
    public MensajeDto<List<ExamenResumenDto>> obtenerExamenes() {
        List<ExamenResumenDto> examenes = publicoRepository.listarExamenes();
        return new MensajeDto<>(false, examenes);
    }

    /**
     * Obtiene la lista de unidades académicas disponibles.
     * @return MensajeDto con la lista de unidades académicas
     */
    public MensajeDto<List<UnidadAcademicaDto>> obtenerUnidades() {
        List<UnidadAcademicaDto> unidades = publicoRepository.listarUnidades();
        return new MensajeDto<>(false, unidades);
    }

    /**
     * Obtiene la lista de unidades académicas asociadas a un docente.
     * @param idUsuario id del docente
     * @return MensajeDto con la lista de unidades académicas del docente
     */
    public MensajeDto<List<UnidadAcademicaDto>> obtenerUnidadesDocente(Long idUsuario) {
        List<UnidadAcademicaDto> unidades = publicoRepository.listarUnidadesDocente(idUsuario);
        return new MensajeDto<>(false, unidades);
    }

    /**
     * Obtiene la lista de temas asociados a una unidad académica específica.
     * @param idUnidad id de la unidad académica
     * @return MensajeDto con la lista de temas de la unidad
     */
    public MensajeDto<List<TemaDto>> obtenerTemasUnidad(Long idUnidad) {
        List<TemaDto> temas = publicoRepository.listarTemasUnidad(idUnidad);
        return new MensajeDto<>(false, temas);
    }

}
