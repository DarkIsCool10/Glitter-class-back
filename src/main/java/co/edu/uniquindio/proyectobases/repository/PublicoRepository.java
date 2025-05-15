package co.edu.uniquindio.proyectobases.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.TemaDto;
import co.edu.uniquindio.proyectobases.dto.TipoPreguntaDto;

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

}
