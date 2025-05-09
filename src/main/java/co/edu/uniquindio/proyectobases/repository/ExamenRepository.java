package co.edu.uniquindio.proyectobases.repository;

import java.util.Optional;
import java.sql.Types;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.ExamenDto;

@Repository
public class ExamenRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExamenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> crearExamen(ExamenDto dto) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("crear_examen")
            .declareParameters(
                new SqlParameter("p_idGrupo", Types.NUMERIC),
                new SqlParameter("p_idDocente", Types.NUMERIC),
                new SqlParameter("p_idTema", Types.NUMERIC),
                new SqlParameter("p_titulo", Types.VARCHAR),
                new SqlParameter("p_descripcion", Types.CLOB),
                new SqlParameter("p_cantidadPreguntas", Types.NUMERIC),
                new SqlParameter("p_preguntasMostradas", Types.NUMERIC),
                new SqlParameter("p_tiempoLimite", Types.NUMERIC),
                new SqlParameter("p_fechaDisponible", Types.TIMESTAMP),
                new SqlParameter("p_fechaCierre", Types.TIMESTAMP),
                new SqlParameter("p_pesoEnCurso", Types.DOUBLE),
                new SqlParameter("p_umbralAprobacion", Types.DOUBLE),
                new SqlParameter("p_aleatorizarPreguntas", Types.NUMERIC),
                new SqlParameter("p_mostrarResultados", Types.NUMERIC),
                new SqlParameter("p_idUnidad", Types.NUMERIC),
                new SqlParameter("p_idEstado", Types.NUMERIC),
                new SqlOutParameter("p_idExamen", Types.NUMERIC),
                new SqlOutParameter("p_resultado", Types.NUMERIC)
            );

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("p_idGrupo", dto.idGrupo())
            .addValue("p_idDocente", dto.idDocente())
            .addValue("p_idTema", dto.idTema())
            .addValue("p_titulo", dto.titulo())
            .addValue("p_descripcion", dto.descripcion())
            .addValue("p_cantidadPreguntas", dto.cantidadPreguntas())
            .addValue("p_preguntasMostradas", dto.preguntasMostradas())
            .addValue("p_tiempoLimite", dto.tiempoLimite())
            .addValue("p_fechaDisponible", dto.fechaDisponible())
            .addValue("p_fechaCierre", dto.fechaCierre())
            .addValue("p_pesoEnCurso", dto.pesoEnCurso())
            .addValue("p_umbralAprobacion", dto.umbralAprobacion())
            .addValue("p_aleatorizarPreguntas", dto.aleatorizarPreguntas())
            .addValue("p_mostrarResultados", dto.mostrarResultados())
            .addValue("p_idUnidad", dto.idUnidad())
            .addValue("p_idEstado", dto.idEstado());

        Map<String, Object> result = jdbcCall.execute(params);

        Number estado = (Number) result.get("p_resultado");
        Number idExamen = (Number) result.get("p_idExamen");

        if (estado != null && estado.intValue() == 1) {
            return Optional.ofNullable(idExamen != null ? idExamen.longValue() : null);
        }

        return Optional.empty();
    }
}

