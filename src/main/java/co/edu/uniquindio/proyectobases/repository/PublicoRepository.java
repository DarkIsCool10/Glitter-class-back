package co.edu.uniquindio.proyectobases.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.CategoriaDto;
import co.edu.uniquindio.proyectobases.dto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.TipoPreguntaDto;

@Repository
public class PublicoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PublicoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<CategoriaDto> listarCategorias(){
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("listar_categorias")
            .withoutProcedureColumnMetaDataAccess()
            .returningResultSet("p_categorias", (rs, rowNum) -> new CategoriaDto(
                rs.getLong("IDCATEGORIA"),
                rs.getString("NOMBRE")
            ));

        Map<String, Object> result = jdbcCall.execute();
        return (List<CategoriaDto>) result.get("p_categorias");
    }

    @SuppressWarnings("unchecked")
    public List<CursoDto> listarCursos() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("listar_cursos")
            .withoutProcedureColumnMetaDataAccess()
            .returningResultSet("p_cursos", (rs, rowNum) -> new CursoDto(
                rs.getLong("IDCURSO"),
                rs.getString("NOMBRE"),
                rs.getString("DESCRIPCION"),
                rs.getInt("CREDITOS"),
                rs.getString("UNIDADACADEMICA")
            ));

        Map<String, Object> result = jdbcCall.execute();
        return (List<CursoDto>) result.get("p_cursos");
    }
    
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

}
