package co.edu.uniquindio.proyectobases.repository;

import java.util.Optional;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.ExamenDto.CrearExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.PreguntaExamenDto;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Repository
public class ExamenRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public ExamenRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @SuppressWarnings("deprecation")
        public Optional<Long> crearExamen(CrearExamenDto dto) {

        if (dto.listaPreguntas() == null || dto.listaPreguntas().isEmpty()) {
            throw new IllegalArgumentException("Debe incluir al menos una pregunta en el examen.");
        }

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(oracle.jdbc.OracleConnection.class);
            ARRAY array = crearArrayPreguntas(dto.listaPreguntas(), oracleConn);

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
                    new SqlParameter("p_pesoEnCurso", Types.NUMERIC),
                    new SqlParameter("p_umbralAprobacion", Types.NUMERIC),
                    new SqlParameter("p_aleatorizar", Types.NUMERIC),
                    new SqlParameter("p_mostrarResultados", Types.NUMERIC),
                    new SqlParameter("p_idUnidad", Types.NUMERIC),
                    new SqlParameter("p_idEstado", Types.NUMERIC),
                    new SqlParameter("p_listaPreguntas", oracle.jdbc.OracleTypes.ARRAY, "T_PREGUNTA_EXAMEN_TABLA"),
                    new SqlOutParameter("p_idExamen", Types.NUMERIC),
                    new SqlOutParameter("p_resultado", Types.NUMERIC)
                );

            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_idGrupo", dto.idGrupo())
                .addValue("p_idDocente", dto.idDocente())
                .addValue("p_idTema", dto.idTema())
                .addValue("p_titulo", dto.titulo())
                .addValue("p_descripcion", dto.descripcion())
                .addValue("p_cantidadPreguntas", dto.listaPreguntas().size())
                .addValue("p_preguntasMostradas", dto.preguntasMostradas())
                .addValue("p_tiempoLimite", dto.tiempoLimite())
                .addValue("p_fechaDisponible", dto.fechaDisponible())
                .addValue("p_fechaCierre", dto.fechaCierre())
                .addValue("p_pesoEnCurso", dto.pesoEnCurso())
                .addValue("p_umbralAprobacion", dto.umbralAprobacion())
                .addValue("p_aleatorizar", dto.aleatorizarPreguntas())
                .addValue("p_mostrarResultados", dto.mostrarResultados())
                .addValue("p_idUnidad", dto.idUnidad())
                .addValue("p_idEstado", dto.idEstado())
                .addValue("p_listaPreguntas", array);

            Map<String, Object> result = jdbcCall.execute(params);

            Number resultado = (Number) result.get("p_resultado");
            Number idExamen = (Number) result.get("p_idExamen");

            return resultado != null && resultado.intValue() == 1
                ? Optional.ofNullable(idExamen).map(Number::longValue)
                : Optional.empty();

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("deprecation")
    private ARRAY crearArrayPreguntas(List<PreguntaExamenDto> lista, OracleConnection oracleConn) throws SQLException {
        StructDescriptor structDesc = StructDescriptor.createDescriptor("T_PREGUNTA_EXAMEN", oracleConn);
        STRUCT[] structArray = lista.stream()
            .map(p -> {
                try {
                    return new STRUCT(structDesc, oracleConn, new Object[]{p.idPregunta(), p.porcentaje()});
                } catch (SQLException e) {
                    throw new RuntimeException("Error creando STRUCT para la pregunta", e);
                }
            }).toArray(STRUCT[]::new);

        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("T_PREGUNTA_EXAMEN_TABLA", oracleConn);
        return new ARRAY(arrayDescriptor, oracleConn, structArray);
    }


}

