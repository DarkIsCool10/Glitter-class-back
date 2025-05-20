package co.edu.uniquindio.proyectobases.repository;

import java.sql.Timestamp;
import java.sql.Types;
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
import co.edu.uniquindio.proyectobases.dto.CursosDto.InfoCursosDocenteDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenResumenDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TemaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TipoPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.UnidadAcademicaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.VisibilidadDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.UsuarioDto.UsuarioDetalleDto;

@Repository
public class PublicoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PublicoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public List<TemaDto> listarTemas(){
        String sql = "SELECT idTema, nombre FROM Tema";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TemaDto(
            rs.getLong("idTema"),
            rs.getString("nombre")
        ));
    }
    
    public List<DificultadDto> listarDificultades() {
        String sql = "SELECT idDificultad, nombre FROM DificultadPregunta";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new DificultadDto(
            rs.getLong("idDificultad"),
            rs.getString("nombre")
        ));
    }

    public List<TipoPreguntaDto> listarTipos() {
        String sql = "SELECT idTipo, nombre FROM TipoPregunta";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TipoPreguntaDto(
            rs.getLong("idTipo"),
            rs.getString("nombre")
        ));
    }

    @SuppressWarnings("deprecation")
    public List<CursoDto> listarCursosEstudiante(Long idUsuario) {
        String sql = "SELECT * FROM vista_estudiante_curso WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new CursoDto(
            rs.getLong("idUsuario"),
            rs.getLong("idCurso"),
            rs.getString("nombreCurso"),
            rs.getInt("creditos"),
            rs.getString("unidadAcademica"),
            rs.getLong("idGrupo"),
            rs.getString("nombreGrupo"),
            rs.getString("nombreDocente")
        ));
    }

    @SuppressWarnings("deprecation")
    public List<CursoDto> listarCursosDocente(Long idUsuario) {
        String sql = "SELECT * FROM vista_docente_curso WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new CursoDto(
            rs.getLong("idUsuario"),
            rs.getLong("idCurso"),
            rs.getString("nombreCurso"),
            rs.getInt("creditos"),
            rs.getString("unidadAcademica"),
            rs.getLong("idGrupo"),
            rs.getString("nombreGrupo"),
            rs.getString("nombreDocente")
        ));
    }

    public List<VisibilidadDto> listarVisibilidades() {
        String sql = "SELECT idVisibilidad, nombre FROM VisibilidadPregunta";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new VisibilidadDto(
            rs.getLong("idVisibilidad"),
            rs.getString("nombre")
        ));
    }

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
                eg.nombre AS estado
            FROM Pregunta p
            JOIN Tema t ON p.idTema = t.idTema
            JOIN VisibilidadPregunta v ON p.idVisibilidad = v.idVisibilidad
            JOIN DificultadPregunta d ON p.idDificultad = d.idDificultad
            JOIN Usuario u ON p.idDocente = u.idUsuario
            JOIN UnidadAcademica ua ON p.idUnidad = ua.idUnidad
            JOIN TipoPregunta tp ON p.idTipo = tp.idTipo
            JOIN EstadoGeneral eg ON p.idEstado = eg.idEstado
            WHERE v.nombre = 'publica'
        """;
    
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ObtenerPreguntaDto(
            rs.getLong("idPregunta"),
            rs.getString("enunciado"),
            rs.getString("tema"),
            rs.getString("visibilidad"),
            rs.getString("dificultad"),
            rs.getString("docente"),
            rs.getString("unidadAcademica"),
            rs.getString("tipoPregunta"),
            rs.getDouble("porcentajeNota"),
            rs.getTimestamp("fechaCreacion").toLocalDateTime(),
            rs.getString("estado")
        ));
    }
    
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


    public List<UnidadAcademicaDto> listarUnidades() {
        String sql = "SELECT idUnidad, nombre FROM UnidadAcademica";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UnidadAcademicaDto(
            rs.getLong("idUnidad"),
            rs.getString("nombre")
        ));
    }

    @SuppressWarnings("deprecation")
    public List<UnidadAcademicaDto> listarUnidadesDocente(Long idUsuario) {
        String sql = """
                        SELECT 
                        ua.idUnidad, 
                        ua.nombre 
                        FROM Usuario u 
                        JOIN UnidadAcademica ua ON u.idUnidad = ua.idUnidad 
                        WHERE u.idUsuario = ?""";
        return jdbcTemplate.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new UnidadAcademicaDto(
            rs.getLong("idUnidad"),
            rs.getString("nombre")
        ));
    }

    @SuppressWarnings("deprecation")
    public List<TemaDto> listarTemasUnidad(Long idUnidad){
        String sql = """
                        SELECT 
                        t.idTema, 
                        t.nombre 
                        FROM Tema t
                        JOIN Curso c ON t.idCurso = c.idCurso
                        WHERE c.idUnidad = ?""";
        return jdbcTemplate.query(sql, new Object[]{idUnidad}, (rs, rowNum) -> new TemaDto(
            rs.getLong("idTema"),
            rs.getString("nombre")
        ));
    }


    @SuppressWarnings("deprecation")
    public List<InfoCursosDocenteDto> listarInfoCursosDocente(Long idUsuario) {
        String sql = """
                        SELECT
                        u.idUsuario,
                        c.idCurso, 
                        c.nombre AS nombreCurso, 
                        g.idGrupo, 
                        g.nombre AS nombreGrupo 
                        FROM Usuario u 
                        JOIN Grupo g ON u.idUsuario = g.idDocente 
                        JOIN Curso c ON g.idCurso = c.idCurso 
                        WHERE u.idUsuario = ?""";
        return jdbcTemplate.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new InfoCursosDocenteDto(
            rs.getLong("idUsuario"),
            rs.getLong("idCurso"),
            rs.getString("nombreCurso"),
            rs.getLong("idGrupo"),
            rs.getString("nombreGrupo")
        ));
    }

}
