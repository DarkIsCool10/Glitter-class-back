package co.edu.uniquindio.proyectobases.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.CursoDto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenResumenDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TemaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TipoPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.VisibilidadDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaPublicaDto;

@Repository
public class PublicoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PublicoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<TemaDto> listarTemas(){
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("listar_temas")
            .withoutProcedureColumnMetaDataAccess()
            .returningResultSet("p_temas", (rs, rowNum) -> new TemaDto(
                rs.getLong("IDTEMA"),
                rs.getString("NOMBRE")
            ));

        Map<String, Object> result = jdbcCall.execute();
        return (List<TemaDto>) result.get("p_temas");
    }

    // @SuppressWarnings("unchecked")
    // public List<CursoDto> listarCursos() {
    //     SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
    //         .withProcedureName("listar_cursos")
    //         .withoutProcedureColumnMetaDataAccess()
    //         .returningResultSet("p_cursos", (rs, rowNum) -> new CursoDto(
    //             rs.getLong("IDCURSO"),
    //             rs.getString("NOMBRE"),
    //             rs.getString("DESCRIPCION"),
    //             rs.getInt("CREDITOS"),
    //             rs.getString("UNIDADACADEMICA")
    //         ));

    //     Map<String, Object> result = jdbcCall.execute();
    //     return (List<CursoDto>) result.get("p_cursos");
    // }
    
    @SuppressWarnings("unchecked")
    public List<DificultadDto> listarDificultades() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("listar_dificultades")
            .withoutProcedureColumnMetaDataAccess()
            .returningResultSet("p_dificultades", (rs, rowNum) -> new DificultadDto(
                rs.getLong("IDDIFICULTAD"),
                rs.getString("NOMBRE")
            ));

        Map<String, Object> result = jdbcCall.execute();
        return (List<DificultadDto>) result.get("p_dificultades");
    }

    @SuppressWarnings("unchecked")
    public List<TipoPreguntaDto> listarTipos() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("listar_tipos")
            .withoutProcedureColumnMetaDataAccess()
            .returningResultSet("p_tipos", (rs, rowNum) -> new TipoPreguntaDto(
                rs.getLong("IDTIPO"),
                rs.getString("NOMBRE")
            ));

        Map<String, Object> result = jdbcCall.execute();
        return (List<TipoPreguntaDto>) result.get("p_tipos");
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

    public List<PreguntaPublicaDto> listarPreguntasPublicas() {
        String sql = """
            SELECT 
                p.idPregunta,
                p.enunciado,
                t.nombre AS tema,
                v.nombre AS visibilidad,
                d.nombre AS dificultad,
                u.nombre || ' ' || u.apellido AS docente,
                ua.nombre AS unidadAcademica,
                tp.nombre AS tipoPregunta
            FROM Pregunta p
            JOIN Tema t ON p.idTema = t.idTema
            JOIN VisibilidadPregunta v ON p.idVisibilidad = v.idVisibilidad
            JOIN DificultadPregunta d ON p.idDificultad = d.idDificultad
            JOIN Usuario u ON p.idDocente = u.idUsuario
            JOIN UnidadAcademica ua ON p.idUnidad = ua.idUnidad
            JOIN TipoPregunta tp ON p.idTipo = tp.idTipo
            WHERE v.nombre = 'publica'
        """;
    
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PreguntaPublicaDto(
            rs.getLong("idPregunta"),
            rs.getString("enunciado"),
            rs.getString("tema"),
            rs.getString("visibilidad"),
            rs.getString("dificultad"),
            rs.getString("docente"),
            rs.getString("unidadAcademica"),
            rs.getString("tipoPregunta")
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

}
