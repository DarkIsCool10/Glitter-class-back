package co.edu.uniquindio.proyectobases.repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.CursosDto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenResumenDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TemaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TipoPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.UnidadAcademicaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.VisibilidadDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerOpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PublicoDto.ObtenerGruposIdDto;
import co.edu.uniquindio.proyectobases.dto.UsuarioDto.UsuarioDetalleDto;

/**
 * Repositorio para operaciones de consulta pública y paramétrica de la plataforma.
 * Permite obtener información de usuarios, cursos, exámenes, preguntas, temas, dificultades, tipos, unidades académicas y visibilidades.
 * Utiliza JdbcTemplate para interactuar con la base de datos mediante consultas y procedimientos almacenados.
 */
@Repository
public class PublicoRepository {

    /**
     * JdbcTemplate para ejecutar consultas y procedimientos almacenados en la base de datos.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor con inyección de dependencias.
     * @param jdbcTemplate plantilla JDBC para operaciones de base de datos
     */
    public PublicoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Obtiene el detalle de un usuario dado su id, llamando a un procedimiento almacenado.
     * @param idUsuario id del usuario a consultar
     * @return Optional con el detalle del usuario si existe, vacío en caso contrario
     */
    public Optional<UsuarioDetalleDto> obtenerUsuarioPorId(Long idUsuario) {
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName("obtener_usuario_detalle")
        .declareParameters(
            new SqlParameter("p_idUsuario", Types.NUMERIC),
            new SqlOutParameter("p_nombre", Types.VARCHAR),
            new SqlOutParameter("p_apellido", Types.VARCHAR),
            new SqlOutParameter("p_correo", Types.VARCHAR),
            new SqlOutParameter("p_idUnidad", Types.NUMERIC),
            new SqlOutParameter("p_nombreUnidad", Types.VARCHAR),
            new SqlOutParameter("p_fechaRegistro", Types.TIMESTAMP),
            new SqlOutParameter("p_estado", Types.VARCHAR),
            new SqlOutParameter("p_idRol", Types.NUMERIC),
            new SqlOutParameter("p_resultado", Types.INTEGER)
        );

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("p_idUsuario", idUsuario);

        Map<String, Object> result = jdbcCall.execute(params);

        Integer resultado = (Integer) result.get("p_resultado");
        if (resultado != null && resultado == 1) {
            return Optional.of(
                new UsuarioDetalleDto(
                    idUsuario,
                    (String) result.get("p_nombre"),
                    (String) result.get("p_apellido"),
                    (String) result.get("p_correo"),
                    ((Number) result.get("p_idUnidad")).longValue(),
                    (String) result.get("p_nombreUnidad"),
                    ((Timestamp) result.get("p_fechaRegistro")).toLocalDateTime(),
                    (String) result.get("p_estado"),
                    ((Number) result.get("p_idRol")).longValue()
                )
            );
        }

        return Optional.empty();
    }

    /**
     * Lista todos los temas registrados en la base de datos.
     * @return lista de temas
     */
    public List<TemaDto> listarTemas(){
        String sql = "SELECT idTema, nombre FROM Tema";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TemaDto(
            rs.getLong("idTema"),
            rs.getString("nombre")
        ));
    }
    
    /**
     * Lista todas las dificultades de preguntas disponibles.
     * @return lista de dificultades
     */
    public List<DificultadDto> listarDificultades() {
        String sql = "SELECT idDificultad, nombre FROM DificultadPregunta";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new DificultadDto(
            rs.getLong("idDificultad"),
            rs.getString("nombre")
        ));
    }

    /**
     * Lista todos los tipos de preguntas disponibles.
     * @return lista de tipos de pregunta
     */
    public List<TipoPreguntaDto> listarTipos() {
        String sql = "SELECT idTipo, nombre FROM TipoPregunta";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TipoPreguntaDto(
            rs.getLong("idTipo"),
            rs.getString("nombre")
        ));
    }

    /**
     * Lista los cursos en los que está inscrito un estudiante, según su id.
     * @param idUsuario id del estudiante
     * @return lista de cursos del estudiante
     */
    public List<CursoDto> listarCursosEstudiante(Long idUsuario) {
        String sql = "SELECT * FROM vista_estudiante_curso WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CursoDto(
            rs.getLong("idUsuario"),
            rs.getLong("idCurso"),
            rs.getString("nombreCurso"),
            rs.getInt("creditos"),
            rs.getString("unidadAcademica"),
            rs.getLong("idGrupo"),
            rs.getString("nombreGrupo"),
            rs.getString("nombreDocente")
        ), idUsuario);
    }

    /**
     * Lista los cursos dictados por un docente, según su id.
     * @param idUsuario id del docente
     * @return lista de cursos del docente
     */
    public List<CursoDto> listarCursosDocente(Long idUsuario) {
        String sql = "SELECT * FROM vista_docente_curso WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CursoDto(
            rs.getLong("idUsuario"),
            rs.getLong("idCurso"),
            rs.getString("nombreCurso"),
            rs.getInt("creditos"),
            rs.getString("unidadAcademica"),
            rs.getLong("idGrupo"),
            rs.getString("nombreGrupo"),
            rs.getString("nombreDocente")
        ), idUsuario);
    }

    /**
     * Lista todas las visibilidades posibles para una pregunta.
     * @return lista de visibilidades
     */
    public List<VisibilidadDto> listarVisibilidades() {
        String sql = "SELECT idVisibilidad, nombre FROM VisibilidadPregunta";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new VisibilidadDto(
            rs.getLong("idVisibilidad"),
            rs.getString("nombre")
        ));
    }

    /**
     * Lista todas las preguntas públicas registradas en el sistema.
     * @return lista de preguntas públicas
     */
    public List<ObtenerPreguntaDto> listarPreguntasPublicas() {
        String sql = """
            SELECT 
                p.idPregunta,
                p.enunciado,
                t.nombre AS tema,
                v.nombre AS visibilidad,
                d.nombre AS dificultad,
                u.nombre || ' ' || u.apellido AS docente,
                ua.nombre AS unidadAcademica,
                tp.nombre AS tipoPregunta,
                p.porcentajeNota,
                p.fechaCreacion,
                eg.nombre AS estado,
                o.idOpcion,
                o.textoOpcion,
                o.idTipoRespuesta
            FROM Pregunta p
            JOIN Tema t ON p.idTema = t.idTema
            JOIN VisibilidadPregunta v ON p.idVisibilidad = v.idVisibilidad
            JOIN DificultadPregunta d ON p.idDificultad = d.idDificultad
            JOIN Usuario u ON p.idDocente = u.idUsuario
            JOIN UnidadAcademica ua ON p.idUnidad = ua.idUnidad
            JOIN TipoPregunta tp ON p.idTipo = tp.idTipo
            JOIN EstadoGeneral eg ON p.idEstado = eg.idEstado
            JOIN OpcionRespuesta o ON p.idPregunta = o.idPregunta
            WHERE v.nombre = 'publica'
        """;
    
        Map<Long, ObtenerPreguntaDto> preguntasMap = new LinkedHashMap<>();
        jdbcTemplate.query(sql, rs -> {
            Long idPregunta = rs.getLong("idPregunta");
            ObtenerPreguntaDto pregunta = preguntasMap.get(idPregunta);
    
            if (pregunta == null) {
                pregunta = new ObtenerPreguntaDto(
                    idPregunta,
                    rs.getString("enunciado"),
                    rs.getString("tema"),
                    rs.getString("visibilidad"),
                    rs.getString("dificultad"),
                    rs.getString("docente"),
                    rs.getString("unidadAcademica"),
                    rs.getString("tipoPregunta"),
                    rs.getDouble("porcentajeNota"),
                    rs.getTimestamp("fechaCreacion"),
                    rs.getString("estado"),
                    new ArrayList<>()
                );
                preguntasMap.put(idPregunta, pregunta);
            }
    
            // Agregar la opción a la lista de opciones de la pregunta
            List<ObtenerOpcionRespuestaDto> opciones = pregunta.opciones();
            opciones.add(new ObtenerOpcionRespuestaDto(
                rs.getLong("idOpcion"),
                rs.getString("textoOpcion"),
                rs.getLong("idTipoRespuesta")
            ));
        });
    
        return new ArrayList<>(preguntasMap.values());
    }
    
    /**
     * Lista todos los exámenes registrados en el sistema con información resumida.
     * @return lista de exámenes
     */
    public List<ExamenResumenDto> listarExamenes() {
        String sql = """
            SELECT 
                e.idExamen,
                g.nombre AS grupo,
                d.nombre || ' ' || d.apellido AS docente,
                t.nombre AS tema,
                e.titulo,
                e.descripcion,
                e.cantidadPreguntas,
                e.tiempoLimite,
                TO_CHAR(e.fechaDisponible, 'YYYY-MM-DD"T"HH24:MI:SS') AS fechaDisponible,
                TO_CHAR(e.fechaCierre, 'YYYY-MM-DD"T"HH24:MI:SS') AS fechaCierre,
                ua.nombre AS unidadAcademica
            FROM Examen e
            JOIN Grupo g ON e.idGrupo = g.idGrupo
            JOIN Usuario d ON e.idDocente = d.idUsuario
            LEFT JOIN Tema t ON e.idTema = t.idTema
            JOIN UnidadAcademica ua ON e.idUnidad = ua.idUnidad
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ExamenResumenDto(
            rs.getLong("idExamen"),
            rs.getString("grupo"),
            rs.getString("docente"),
            rs.getString("tema"),
            rs.getString("titulo"),
            rs.getString("descripcion"),
            rs.getInt("cantidadPreguntas"),
            rs.getInt("tiempoLimite"),
            rs.getString("fechaDisponible"),
            rs.getString("fechaCierre"),
            rs.getString("unidadAcademica")
        ));
    }


    /**
     * Lista todas las unidades académicas existentes.
     * @return lista de unidades académicas
     */
    public List<UnidadAcademicaDto> listarUnidades() {
        String sql = "SELECT idUnidad, nombre FROM UnidadAcademica";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UnidadAcademicaDto(
            rs.getLong("idUnidad"),
            rs.getString("nombre")
        ));
    }

    /**
     * Lista la unidad académica asociada a un docente según su id.
     * @param idUsuario id del docente
     * @return lista de unidades académicas del docente
     */
    public List<UnidadAcademicaDto> listarUnidadesDocente(Long idUsuario) {
        String sql = """
                        SELECT 
                        ua.idUnidad, 
                        ua.nombre 
                        FROM Usuario u 
                        JOIN UnidadAcademica ua ON u.idUnidad = ua.idUnidad 
                        WHERE u.idUsuario = ?""";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UnidadAcademicaDto(
            rs.getLong("idUnidad"),
            rs.getString("nombre")
        ),idUsuario);
    }

    /**
     * Lista los temas asociados a una unidad académica específica.
     * @param idUnidad id de la unidad académica
     * @return lista de temas de la unidad
     */
    public List<TemaDto> listarTemasUnidad(Long idUnidad){
        String sql = """
                        SELECT 
                        t.idTema, 
                        t.nombre 
                        FROM Tema t
                        JOIN Curso c ON t.idCurso = c.idCurso
                        WHERE c.idUnidad = ?""";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TemaDto(
            rs.getLong("idTema"),
            rs.getString("nombre")
        ),idUnidad);
    }

    /**
     * Lista los grupos asociados a un docente según su id.
     * @param idUsuario id del docente
     * @return lista de grupos del docente
     */
    public List<ObtenerGruposIdDto> listarGruposDocente(Long idUsuario) {
        String sql = """
                        SELECT 
                        g.idGrupo, 
                        g.nombre,
                        g.idCurso,
                        c.nombre AS nombreCurso
                        FROM Grupo g
                        JOIN Curso c ON g.idCurso = c.idCurso
                        JOIN Usuario u ON g.idDocente = u.idUsuario  
                        WHERE u.idUsuario = ?
                        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ObtenerGruposIdDto(
            rs.getLong("idGrupo"),
            rs.getString("nombre"),
            rs.getLong("idCurso"),
            rs.getString("nombreCurso")
        ), idUsuario);
    }

    /**
     * Lista los grupos en los que está inscrito un estudiante.
     *
     * @param idUsuario identificador del estudiante
     * @return lista de grupos en los que participa el estudiante
     */
    public List<ObtenerGruposIdDto> listarGruposEstudiante(Long idUsuario) {
        String sql = """
                        SELECT 
                        g.idGrupo, 
                        g.nombre,
                        g.idCurso,
                        c.nombre AS nombreCurso
                        FROM Grupo g
                        JOIN Curso c ON c.idCurso = g.idCurso
                        JOIN GrupoUsuario gu ON g.idGrupo = gu.idGrupo
                        JOIN Usuario u ON gu.idUsuario = u.idUsuario  
                        WHERE u.idUsuario = ?
                        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ObtenerGruposIdDto(
            rs.getLong("idGrupo"),
            rs.getString("nombre"),
            rs.getLong("idCurso"),
            rs.getString("nombreCurso")
        ), idUsuario);
    }


}
