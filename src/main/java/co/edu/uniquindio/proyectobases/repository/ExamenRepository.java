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
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ObtenerExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.PreguntaExamenDto;
import co.edu.uniquindio.proyectobases.exception.ExamenException;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 * Repositorio encargado de las operaciones relacionadas con la creación de exámenes.
 * Utiliza procedimientos almacenados en Oracle y manejo de tipos compuestos para registrar exámenes y sus preguntas.
 */
@Repository
public class ExamenRepository {

    /**
     * JdbcTemplate para operaciones JDBC sobre la base de datos.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * DataSource para obtener conexiones directas a la base de datos Oracle.
     */
    private final DataSource dataSource;

    /**
     * Constructor con inyección de dependencias.
     * @param jdbcTemplate plantilla JDBC para operaciones de base de datos
     * @param dataSource fuente de datos para conexiones Oracle
     */
    public ExamenRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    /**
     * Crea un nuevo examen en la base de datos utilizando un procedimiento almacenado Oracle.
     * Convierte la lista de preguntas a un arreglo Oracle para enviarlo como parámetro compuesto.
     * @param dto DTO con los datos necesarios para crear el examen
     * @return Optional con el id del examen creado si fue exitoso, vacío en caso contrario
     * @throws ExamenException si no se incluyen preguntas en el examen
     */
    @SuppressWarnings("deprecation")
    public Optional<Long> crearExamen(CrearExamenDto dto) throws ExamenException {

        // Validación: el examen debe tener al menos una pregunta
        if (dto.listaPreguntas() == null || dto.listaPreguntas().isEmpty()) {
            throw new ExamenException("Debe incluir al menos una pregunta en el examen.");
        }

        try (Connection conn = dataSource.getConnection()) {
            // Obtiene la conexión Oracle y crea el arreglo de preguntas como tipo compuesto
            OracleConnection oracleConn = conn.unwrap(oracle.jdbc.OracleConnection.class);
            ARRAY array = crearArrayPreguntas(dto.listaPreguntas(), oracleConn);

            // Configura el llamado al procedimiento almacenado 'crear_examen' y sus parámetros
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

            // Prepara los parámetros de entrada para el procedimiento
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

            // Ejecuta el procedimiento y obtiene el resultado
            Map<String, Object> result = jdbcCall.execute(params);

            // Recupera el resultado y el id del examen creado
            Number resultado = (Number) result.get("p_resultado");
            Number idExamen = (Number) result.get("p_idExamen");

            // Retorna el id del examen si la operación fue exitosa
            return resultado != null && resultado.intValue() == 1
                ? Optional.ofNullable(idExamen).map(Number::longValue)
                : Optional.empty();

        } catch (Exception e) {
            // Si ocurre un error, retorna vacío
            return Optional.empty();
        }
    }

    /**
     * Crea un arreglo Oracle (ARRAY) de preguntas para ser enviado como parámetro compuesto al procedimiento almacenado.
     * Cada pregunta se convierte en un STRUCT del tipo definido en la base de datos.
     * @param lista lista de preguntas del examen
     * @param oracleConn conexión Oracle para crear descriptores de tipos
     * @return arreglo Oracle de preguntas
     * @throws SQLException si ocurre un error al crear los descriptores o el arreglo
     */
    @SuppressWarnings("deprecation")
    private ARRAY crearArrayPreguntas(List<PreguntaExamenDto> lista, OracleConnection oracleConn) throws SQLException {
        // Descriptor para el tipo STRUCT de pregunta
        StructDescriptor structDesc = StructDescriptor.createDescriptor("T_PREGUNTA_EXAMEN", oracleConn);
        STRUCT[] structArray = lista.stream()
            .map(p -> {
                try {
                    // Cada pregunta se transforma en un STRUCT con idPregunta y porcentaje
                    return new STRUCT(structDesc, oracleConn, new Object[]{p.idPregunta(), p.porcentaje()});
                } catch (SQLException e) {
                    throw new RuntimeException("Error creando STRUCT para la pregunta", e);
                }
            }).toArray(STRUCT[]::new);

        // Descriptor para el tipo ARRAY de preguntas
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("T_PREGUNTA_EXAMEN_TABLA", oracleConn);
        // Retorna el arreglo Oracle de STRUCTs
        return new ARRAY(arrayDescriptor, oracleConn, structArray);
    }

    /**
     * Lista todos los exámenes asociados a un docente en la base de datos.
     * Si la lista es exitosa, retorna la lista de exámenes; en caso de error, retorna null y marca el mensaje como error.
     *
     * @param idDocente identificador del docente
     * @return Optional con la lista de exámenes si la operación fue exitosa
     * @throws ExamenException si ocurre un error al listar los exámenes
     */
    public List<ObtenerExamenDto> listarExamenesDocente(Long idDocente) throws ExamenException {
        String sql = """
            SELECT 
                e.idExamen,
                e.idTema,
                t.nombre AS tema,
                e.titulo,
                e.descripcion,
                e.cantidadPreguntas,
                e.tiempoLimite,
                e.fechaDisponible,
                e.fechaCierre,
                e.pesoEnCurso,
                e.umbralAprobacion,
                e.aleatorizarPreguntas,
                e.mostrarResultados,
                e.idUnidad,
                ua.nombre AS unidadAcademica,
                eg.nombre AS estado
            FROM Examen e
            JOIN UnidadAcademica ua ON e.idUnidad = ua.idUnidad
            JOIN EstadoGeneral eg ON e.idEstado = eg.idEstado
            JOIN Tema t ON e.idTema = t.idTema
            WHERE e.idDocente = ?
            ORDER BY e.fechaDisponible DESC
            """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ObtenerExamenDto(
            rs.getLong("idExamen"),
            rs.getLong("idTema"),
            rs.getString("tema"),
            rs.getString("titulo"),
            rs.getString("descripcion"),
            rs.getInt("cantidadPreguntas"),
            rs.getInt("tiempoLimite"),
            rs.getTimestamp("fechaDisponible"),
            rs.getTimestamp("fechaCierre"),
            rs.getDouble("pesoEnCurso"),
            rs.getDouble("umbralAprobacion"),
            rs.getInt("aleatorizarPreguntas"),
            rs.getInt("mostrarResultados"),
            rs.getLong("idUnidad"),
            rs.getString("unidadAcademica"),
            rs.getString("estado")
        ), idDocente);
    }

}

