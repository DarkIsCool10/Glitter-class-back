package co.edu.uniquindio.proyectobases.repository;

import java.util.Optional;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.sql.Types;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto;

@Repository
public class PreguntaRepository {

    private final JdbcTemplate jdbcTemplate;

    public PreguntaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> crearPregunta(PreguntaDto dto) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("crear_pregunta")
                .declareParameters(
                    new SqlParameter("p_enunciado", Types.CLOB),
                    new SqlParameter("p_idTema", Types.NUMERIC),
                    new SqlParameter("p_idDificultad", Types.NUMERIC),
                    new SqlParameter("p_idTipo", Types.NUMERIC),
                    new SqlParameter("p_tiempoMaximo", Types.NUMERIC),
                    new SqlParameter("p_porcentajeNota", Types.DOUBLE),
                    new SqlParameter("p_idVisibilidad", Types.NUMERIC),
                    new SqlParameter("p_idDocente", Types.NUMERIC),
                    new SqlParameter("p_idUnidad", Types.NUMERIC),
                    new SqlParameter("p_idEstado", Types.NUMERIC), // CAMBIADO
                    new SqlOutParameter("p_idPregunta", Types.NUMERIC),
                    new SqlOutParameter("p_resultado", Types.NUMERIC)
                );
    
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_enunciado", dto.enunciado())
                .addValue("p_idTema", dto.idTema())
                .addValue("p_idDificultad", dto.idDificultad())
                .addValue("p_idTipo", dto.idTipo())
                .addValue("p_tiempoMaximo", dto.tiempoMaximo())
                .addValue("p_porcentajeNota", dto.porcentajeNota())
                .addValue("p_idVisibilidad", dto.idVisibilidad())
                .addValue("p_idDocente", dto.idDocente())
                .addValue("p_idUnidad", dto.idUnidad())
                .addValue("p_idEstado", dto.idEstado()); // CAMBIADO
    
            Map<String, Object> result = jdbcCall.execute(params);
    
            Number estado = (Number) result.get("p_resultado");
            Number idPregunta = (Number) result.get("p_idPregunta");
    
            if (estado != null && estado.intValue() == 1) {
                return Optional.ofNullable(idPregunta != null ? idPregunta.longValue() : null);
            }
        } catch (Exception e) {
            
        }
    
        return Optional.empty();
    }    
}

