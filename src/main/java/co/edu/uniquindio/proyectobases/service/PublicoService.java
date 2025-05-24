package co.edu.uniquindio.proyectobases.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.PublicoRepository;
import lombok.AllArgsConstructor;
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


/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con la información pública y paramétrica de la plataforma.
 * Interactúa con el repositorio de público para consultar usuarios, temas, dificultades, tipos de pregunta, cursos, visibilidades, exámenes y unidades académicas,
 * retornando los resultados en objetos de dominio para ser usados por los controladores.
 */
@Service
@AllArgsConstructor
public class PublicoService {

    /**
     * Repositorio que provee acceso a la información pública y paramétrica de la base de datos.
     */
    private final PublicoRepository publicoRepository;


    /**
     * Obtiene el detalle de un usuario a partir de su identificador único.
     *
     * @param idUsuario identificador del usuario a consultar
     * @return el detalle del usuario, si existe
     * @throws PublicoException si el usuario no existe
     */
    public UsuarioDetalleDto obtenerUsuario(Long idUsuario) throws PublicoException {
        Optional<UsuarioDetalleDto> usuario = publicoRepository.obtenerUsuarioPorId(idUsuario);
        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new PublicoException("Usuario no encontrado");
        }
    }


    /**
     * Obtiene la lista completa de temas registrados en el sistema.
     *
     * @return lista de temas disponibles
     * @throws PublicoException si ocurre un error al consultar los temas
     */
    public List<TemaDto> obtenerTemas() throws PublicoException {
        return publicoRepository.listarTemas();
    }

    /**
     * Obtiene la lista de dificultades de preguntas disponibles en el sistema.
     *
     * @return lista de dificultades posibles para preguntas
     * @throws PublicoException si ocurre un error al consultar las dificultades
     */
    public List<DificultadDto> obtenerDificultades() throws PublicoException {
        return publicoRepository.listarDificultades();
    }

    /**
     * Obtiene la lista de tipos de preguntas configurados en el sistema.
     *
     * @return lista de tipos de pregunta
     * @throws PublicoException si ocurre un error al consultar los tipos
     */
    public List<TipoPreguntaDto> obtenerTipos() throws PublicoException {
        return publicoRepository.listarTipos();
    }

    /**
     * Obtiene la lista de cursos dictados por un docente específico.
     *
     * @param idUsuario identificador del docente
     * @return lista de cursos asociados al docente
     * @throws PublicoException si ocurre un error al consultar los cursos
     */
    public List<CursoDto> obtenerCursosDocente(Long idUsuario) throws PublicoException {
        return publicoRepository.listarCursosDocente(idUsuario);
    }

    /**
     * Obtiene la lista de cursos en los que está inscrito un estudiante.
     *
     * @param idUsuario identificador del estudiante
     * @return lista de cursos en los que participa el estudiante
     * @throws PublicoException si ocurre un error al consultar los cursos
     */
    public List<CursoDto> obtenerCursosEstudiante(Long idUsuario) throws PublicoException {
        return publicoRepository.listarCursosEstudiante(idUsuario);
    }
    
    /**
     * Obtiene la lista de todas las preguntas públicas disponibles en el sistema.
     *
     * @return lista de preguntas públicas
     * @throws PublicoException si ocurre un error al consultar las preguntas
     */
    public List<ObtenerPreguntaDto> obtenerPreguntasPublicas() throws PublicoException {
        return publicoRepository.listarPreguntasPublicas();
    }

    /**
     * Obtiene la lista de visibilidades posibles para las preguntas.
     *
     * @return lista de tipos de visibilidad
     * @throws PublicoException si ocurre un error al consultar las visibilidades
     */
    public List<VisibilidadDto> obtenerVisibilidades() throws PublicoException {
        return publicoRepository.listarVisibilidades();
    }

    /**
     * Obtiene la lista de exámenes registrados en el sistema.
     *
     * @return lista de exámenes disponibles
     * @throws PublicoException si ocurre un error al consultar los exámenes
     */
    public List<ExamenResumenDto> obtenerExamenes() throws PublicoException {
        return publicoRepository.listarExamenes();
    }

    /**
     * Obtiene la lista de unidades académicas disponibles en la plataforma.
     *
     * @return lista de unidades académicas
     * @throws PublicoException si ocurre un error al consultar las unidades académicas
     */
    public List<UnidadAcademicaDto> obtenerUnidades() throws PublicoException {
        return publicoRepository.listarUnidades();
    }

    /**
     * Obtiene la lista de unidades académicas asociadas a un docente específico.
     *
     * @param idUsuario identificador del docente
     * @return lista de unidades académicas del docente
     * @throws PublicoException si ocurre un error al consultar las unidades académicas
     */
    public List<UnidadAcademicaDto> obtenerUnidadesDocente(Long idUsuario) throws PublicoException {
        return publicoRepository.listarUnidadesDocente(idUsuario);
    }

    /**
     * Obtiene la lista de temas asociados a una unidad académica específica.
     *
     * @param idUnidad identificador de la unidad académica
     * @return lista de temas de la unidad académica
     * @throws PublicoException si ocurre un error al consultar los temas
     */
    public List<TemaDto> obtenerTemasUnidad(Long idUnidad) throws PublicoException {
        return publicoRepository.listarTemasUnidad(idUnidad);
    }

    /**
     * Obtiene la lista de grupos asociados a un docente específico.
     *
     * @param idUsuario identificador del docente
     * @return lista de grupos del docente
     * @throws PublicoException si ocurre un error al consultar los grupos
     */
    public List<ObtenerGruposDocenteDto> obtenerGruposDocente(Long idUsuario) throws PublicoException {
        return publicoRepository.listarGruposDocente(idUsuario);
    }

}
