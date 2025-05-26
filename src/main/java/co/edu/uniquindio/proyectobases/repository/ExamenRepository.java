package co.edu.uniquindio.proyectobases.repository;

import java.util.Optional;

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
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenGrupoDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ObtenerExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.RespuestaCrearExamenDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.cantidadPreguntasDto;
import co.edu.uniquindio.proyectobases.exception.ExamenException;

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
     * Constructor con inyección de dependencias.
     * @param jdbcTemplate plantilla JDBC para operaciones de base de datos
     */
    public ExamenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Crea un nuevo examen en la base de datos utilizando un procedimiento almacenado Oracle.
     * Convierte la lista de preguntas a un arreglo Oracle para enviarlo como parámetro compuesto.
     * @param dto DTO con los datos necesarios para crear el examen
     * @return Optional con el id del examen creado si fue exitoso, vacío en caso contrario
     * @throws ExamenException si no se incluyen preguntas en el examen
     */
    public Optional<RespuestaCrearExamenDto> crearExamen(CrearExamenDto dto) throws ExamenException {

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
                    new SqlParameter("p_idUnidad", Types.NUMERIC),
                    new SqlOutParameter("p_idExamen", Types.NUMERIC),
                    new SqlOutParameter("p_idTemas", Types.NUMERIC),
                    new SqlOutParameter("p_resultado", Types.NUMERIC)
                );

            // Prepara los parámetros de entrada para el procedimiento
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_idGrupo", dto.idGrupo())
                .addValue("p_idDocente", dto.idDocente())
                .addValue("p_idTema", dto.idTema())
                .addValue("p_titulo", dto.titulo())
                .addValue("p_descripcion", dto.descripcion())
                .addValue("p_cantidadPreguntas", null)
                .addValue("p_preguntasMostradas", null)
                .addValue("p_tiempoLimite", dto.tiempoLimite())
                .addValue("p_fechaDisponible", dto.fechaDisponible())
                .addValue("p_fechaCierre", dto.fechaCierre())
                .addValue("p_pesoEnCurso", dto.pesoEnCurso())
                .addValue("p_umbralAprobacion", dto.umbralAprobacion())
                .addValue("p_idUnidad", dto.idUnidad());

            // Ejecuta el procedimiento y obtiene el resultado
            Map<String, Object> result = jdbcCall.execute(params);

            // Recupera el resultado y el id del examen creado
            Number resultado = (Number) result.get("p_resultado");
            Number idExamen = (Number) result.get("p_idExamen");
            Number idTema = (Number) result.get("p_idTemas");

            // Retorna el id del examen si la operación fue exitosa
            if (resultado != null && resultado.intValue() == 1) {
                return Optional.of(new RespuestaCrearExamenDto(idExamen.longValue(), idTema.longValue()));
            }
            return Optional.empty();
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
                e.preguntasMostradas,
                e.tiempoLimite,
                e.fechaDisponible,
                e.fechaCierre,
                e.pesoEnCurso,
                e.umbralAprobacion,
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
            rs.getInt("preguntasMostradas"),
            rs.getInt("tiempoLimite"),
            rs.getTimestamp("fechaDisponible"),
            rs.getTimestamp("fechaCierre"),
            rs.getDouble("pesoEnCurso"),
            rs.getDouble("umbralAprobacion"),
            rs.getLong("idUnidad"),
            rs.getString("unidadAcademica"),
            rs.getString("estado")
        ), idDocente);
    }

    /**
     * Lista todos los exámenes asociados a un grupo en la base de datos.
     * Si la lista es exitosa, retorna la lista de exámenes; en caso de error, retorna null y marca el mensaje como error.
     *
     * @param idGrupo identificador del grupo
     * @return Optional con la lista de exámenes si la operación fue exitosa
     * @throws ExamenException si ocurre un error al listar los exámenes
     */
    public List<ExamenGrupoDto> ListarExamenGrupo(Long idGrupo) throws ExamenException{
        String sql = """
        SELECT 
            e.idExamen,
            e.idTema,
            t.nombre AS tema,
            e.titulo,
            e.descripcion,
            e.cantidadPreguntas,
            e.preguntasMostradas,
            e.tiempoLimite,
            e.fechaDisponible,
            e.fechaCierre,
            e.pesoEnCurso,
            e.umbralAprobacion,
            e.idUnidad,
            ua.nombre AS unidadAcademica,
            eg.nombre AS estado
        FROM Examen e
        JOIN UnidadAcademica ua ON e.idUnidad = ua.idUnidad
        JOIN EstadoGeneral eg ON e.idEstado = eg.idEstado
        JOIN Tema t ON e.idTema = t.idTema
        WHERE e.idGrupo = ?
        ORDER BY e.fechaDisponible DESC
        """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ExamenGrupoDto(
            rs.getLong("idExamen"),
            rs.getLong("idTema"),
            rs.getString("tema"),
            rs.getString("titulo"),
            rs.getString("descripcion"),
            rs.getInt("cantidadPreguntas"),
            rs.getInt("preguntasMostradas"),
            rs.getInt("tiempoLimite"),
            rs.getTimestamp("fechaDisponible"),
            rs.getTimestamp("fechaCierre"),
            rs.getDouble("pesoEnCurso"),
            rs.getDouble("umbralAprobacion"),
            rs.getLong("idUnidad"),
            rs.getString("unidadAcademica"),
            rs.getString("estado")
        ), idGrupo);

    }

    /**
     * Agrega una pregunta a un examen en la base de datos.
     * Utiliza el procedimiento almacenado 'agregar_pregunta_a_examen' para agregar la pregunta al examen.
     * Si la operación es exitosa, retorna el id de la pregunta agregada; en caso contrario, retorna Optional.empty().
     *
     * @param idExamen identificador del examen al que se agregará la pregunta
     * @param idPregunta identificador de la pregunta que se agregará al examen
     * @return Optional con el id de la pregunta agregada si la operación fue exitosa
     * @throws ExamenException si ocurre un error al agregar la pregunta al examen
     */
    public int agregarPreguntaExamen(Long idExamen, Long idPregunta) throws ExamenException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("agregar_pregunta_a_examen")
            .declareParameters(
                new SqlParameter("p_idExamen", Types.NUMERIC),
                new SqlParameter("p_idPregunta", Types.NUMERIC),
                new SqlOutParameter("p_resultado", Types.INTEGER)
            );
    
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("p_idExamen", idExamen)
            .addValue("p_idPregunta", idPregunta);
    
        Map<String, Object> result = jdbcCall.execute(params);
        return ((Number) result.get("p_resultado")).intValue();
    }

    /**
     * Actualiza la cantidad de preguntas usando un DTO
     * @param dto DTO con los datos de actualización
     * @return Map con los resultados
     * @throws ExamenException si ocurre un error
     */
    public Map<String, Object> actualizarCantidadPreguntas(cantidadPreguntasDto dto) throws ExamenException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("actualizar_datos_examen")
            .declareParameters(
                new SqlParameter("p_idExamen", Types.NUMERIC),
                new SqlParameter("p_totalPreguntas", Types.NUMERIC),
                new SqlParameter("p_preguntasMostrar", Types.NUMERIC),
                new SqlOutParameter("p_resultado", Types.INTEGER)
            );
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("p_idExamen", dto.idExamen())
            .addValue("p_totalPreguntas", dto.totalPreguntas())
            .addValue("p_preguntasMostrar", dto.preguntasMostrar());
        
        Map<String, Object> result = jdbcCall.execute(params);
        return result;
    }
    
    /**
     * Genera un examen para un estudiante
     * @param idExamen identificador del examen
     * @param idEstudiante identificador del estudiante
     * @return Integer con el resultado
     * @throws ExamenException si ocurre un error
     */
    public int generarExamenEstudiante(Long idExamen, Long idEstudiante) throws ExamenException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("generar_examen_estudiante")
            .declareParameters(
                new SqlParameter("p_idExamen", Types.NUMERIC),
                new SqlParameter("p_idEstudiante", Types.NUMERIC),
                new SqlOutParameter("p_resultado", Types.INTEGER)
            );
    
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("p_idExamen", idExamen)
            .addValue("p_idEstudiante", idEstudiante);
    
        Map<String, Object> result = jdbcCall.execute(params);
    
        Integer resultado = (Integer) result.get("p_resultado");
        if (resultado == null || resultado != 1) {
            throw new ExamenException("No se pudo generar el examen para el estudiante.");
        }
        return resultado;
    }
   
    

    

}

