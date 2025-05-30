package co.edu.uniquindio.proyectobases.repository;

import java.util.Optional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Types;

import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerOpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.exception.PreguntaException;

/**
 * Repositorio encargado de las operaciones relacionadas con preguntas y sus opciones de respuesta.
 * Permite crear preguntas, asociar opciones y consultar preguntas con sus opciones y detalles.
 * Utiliza JdbcTemplate y procedimientos almacenados para interactuar con la base de datos.
 */
@Repository
public class PreguntaRepository {

    /**
     * JdbcTemplate para ejecutar consultas y procedimientos almacenados en la base de datos.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor con inyección de dependencias.
     * @param jdbcTemplate plantilla JDBC para operaciones de base de datos
     */
    public PreguntaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Crea una nueva pregunta en la base de datos utilizando el procedimiento almacenado 'crear_pregunta'.
     * Este método toma los datos de la pregunta desde el DTO, construye los parámetros requeridos y ejecuta el procedimiento.
     * Si la operación es exitosa, retorna el id de la nueva pregunta.
     *
     * @param dto DTO con los datos de la pregunta a crear (enunciado, tema, dificultad, tipo, porcentaje, visibilidad, docente, unidad, estado)
     * @return Optional con el id de la pregunta creada si fue exitosa, vacío en caso contrario
     */
    public Optional<Long> crearPregunta(PreguntaDto dto) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("crear_pregunta")
                .declareParameters(
                    new SqlParameter("p_enunciado", Types.CLOB),
                    new SqlParameter("p_idTema", Types.NUMERIC),
                    new SqlParameter("p_idDificultad", Types.NUMERIC),
                    new SqlParameter("p_idTipo", Types.NUMERIC),
                    new SqlParameter("p_porcentajeNota", Types.DOUBLE),
                    new SqlParameter("p_idVisibilidad", Types.NUMERIC),
                    new SqlParameter("p_idDocente", Types.NUMERIC),
                    new SqlParameter("p_idUnidad", Types.NUMERIC),
                    new SqlParameter("p_idEstado", Types.NUMERIC),
                    new SqlOutParameter("p_idPregunta", Types.NUMERIC),
                    new SqlOutParameter("p_resultado", Types.NUMERIC)
                );
    
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_enunciado", dto.enunciado())
                .addValue("p_idTema", dto.idTema())
                .addValue("p_idDificultad", dto.idDificultad())
                .addValue("p_idTipo", dto.idTipo())
                .addValue("p_porcentajeNota", dto.porcentajeNota())
                .addValue("p_idVisibilidad", dto.idVisibilidad())
                .addValue("p_idDocente", dto.idDocente())
                .addValue("p_idUnidad", dto.idUnidad())
                .addValue("p_idEstado", dto.idEstado()); 
    
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
    
    /**
     * Crea una opción de respuesta asociada a una pregunta utilizando el procedimiento almacenado 'crear_opcion_respuesta'.
     * Envía los datos de la opción y la pregunta correspondiente, y retorna información relevante sobre la opción creada.
     *
     * @param idPregunta id de la pregunta a la que se asocia la opción
     * @param dto DTO con el texto, porcentaje parcial e id del tipo de respuesta de la opción
     * @return id de la opción creada
     */
    public Optional<Long> crearOpcionRespuesta(Long idPregunta, OpcionRespuestaDto dto) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("crear_opcion_respuesta")
                .declareParameters(
                        new SqlParameter("p_idPregunta", Types.NUMERIC),
                        new SqlParameter("p_textoOpcion", Types.CLOB),
                        new SqlParameter("p_idTipoRespuesta", Types.NUMERIC),
                        new SqlParameter("p_textoPareja", Types.CLOB),
                        new SqlOutParameter("p_idOpcion", Types.NUMERIC),
                        new SqlOutParameter("p_resultado", Types.NUMERIC)
                );
        
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_idPregunta", idPregunta)
                    .addValue("p_textoOpcion", dto.textoOpcion())
                    .addValue("p_textoPareja", dto.textoPareja())
                    .addValue("p_idTipoRespuesta", dto.idTipoRespuesta());
        
            Map<String, Object> result = jdbcCall.execute(params);

            int resultado = ((Number) result.get("p_resultado")).intValue();
            
            if (resultado == 1 && result.get("p_idOpcion") != null) {
                return Optional.of(((Number) result.get("p_idOpcion")).longValue());
            }
            
            return Optional.empty();
    }

    /**
     * Obtiene todas las preguntas junto con sus opciones de respuesta asociadas.
     * Realiza una consulta que une las preguntas con sus opciones (si existen),
     * y agrupa los resultados en objetos que contienen la pregunta y su lista de opciones.
     *
     * @return lista de preguntas con sus opciones (puede estar vacía si no existen preguntas)
     */
    public List<ObtenerPreguntaDto> obtenerTodasLasPreguntasConOpciones() {
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
                o.textoPareja,
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
            ORDER BY p.idPregunta
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
                rs.getString("textoPareja"),
                rs.getLong("idTipoRespuesta")
            ));
        });
    
        return new ArrayList<>(preguntasMap.values());
    }
    
    /**
     * Obtiene todas las preguntas creadas por un docente específico, incluyendo detalles como tema, visibilidad, dificultad,
     * nombre del docente, unidad académica, tipo, porcentaje de nota, fecha de creación y estado.
     * Realiza una consulta con múltiples joins para traer toda la información relevante de cada pregunta.
     *
     * @param idDocente id del docente cuyas preguntas se desean listar
     * @return lista de preguntas detalladas del docente (puede estar vacía si no existen preguntas)
     */
    public List<ObtenerPreguntaDto> listarPreguntasDocente(Long idDocente) {
        String sql = """
            SELECT 
                p.idPregunta,
                p.enunciado,
                t.nombre AS tema,
                v.nombre AS visibilidad,
                dp.nombre AS dificultad,
                u.nombre || ' ' || u.apellido AS docente,
                ua.nombre AS unidadAcademica,
                tp.nombre AS tipo,
                p.porcentajeNota,
                p.fechaCreacion,
                eg.nombre AS estado,
                o.idOpcion,
                o.textoOpcion,
                o.textoPareja,
                o.idTipoRespuesta
            FROM Pregunta p
            JOIN Tema t ON p.idTema = t.idTema
            JOIN DificultadPregunta dp ON p.idDificultad = dp.idDificultad
            JOIN Usuario u ON p.idDocente = u.idUsuario
            JOIN TipoPregunta tp ON p.idTipo = tp.idTipo
            JOIN UnidadAcademica ua ON p.idUnidad = ua.idUnidad
            JOIN VisibilidadPregunta v ON p.idVisibilidad = v.idVisibilidad
            JOIN EstadoGeneral eg ON p.idEstado = eg.idEstado
            JOIN OpcionRespuesta o ON p.idPregunta = o.idPregunta
            WHERE p.idDocente = ?
            ORDER BY p.idPregunta, o.idOpcion
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
                    rs.getString("tipo"),
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
                rs.getString("textoPareja"),
                rs.getLong("idTipoRespuesta")
            ));
        }, idDocente);
    
        return new ArrayList<>(preguntasMap.values());
    }

    /**
     * Obtiene una lista de preguntas filtradas por el tema especificado.
     *
     * @param idTema identificador del tema por el cual se filtran las preguntas
     * @return lista de preguntas que pertenecen al tema especificado
     * @throws PreguntaException si ocurre un error al consultar las preguntas
     */
    public List<ObtenerPreguntaDto> listarPreguntasTema(Long idTema) throws PreguntaException {
        String sql = """
            SELECT 
                p.idPregunta,
                p.enunciado,
                t.nombre AS tema,
                v.nombre AS visibilidad,
                dp.nombre AS dificultad,
                u.nombre || ' ' || u.apellido AS docente,
                ua.nombre AS unidadAcademica,
                tp.nombre AS tipo,
                p.porcentajeNota,
                p.fechaCreacion,
                eg.nombre AS estado,
                o.idOpcion,
                o.textoOpcion,
                o.textoPareja,
                o.idTipoRespuesta
            FROM Pregunta p
            JOIN Tema t ON p.idTema = t.idTema
            JOIN DificultadPregunta dp ON p.idDificultad = dp.idDificultad
            JOIN Usuario u ON p.idDocente = u.idUsuario
            JOIN TipoPregunta tp ON p.idTipo = tp.idTipo
            JOIN UnidadAcademica ua ON p.idUnidad = ua.idUnidad
            JOIN VisibilidadPregunta v ON p.idVisibilidad = v.idVisibilidad
            JOIN EstadoGeneral eg ON p.idEstado = eg.idEstado
            JOIN OpcionRespuesta o ON p.idPregunta = o.idPregunta
            WHERE p.idTema = ?
            ORDER BY p.idPregunta, o.idOpcion
            """;
    
        Map<Long, ObtenerPreguntaDto> preguntasMap = new LinkedHashMap<>();
    
        jdbcTemplate.query(sql, ps -> ps.setLong(1, idTema), rs -> {
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
                    rs.getString("tipo"),
                    rs.getDouble("porcentajeNota"),
                    rs.getTimestamp("fechaCreacion"),
                    rs.getString("estado"),
                    new ArrayList<>()
                );
                preguntasMap.put(idPregunta, pregunta);
            }
    
            // Agregar opción correspondiente
            pregunta.opciones().add(new ObtenerOpcionRespuestaDto(
                rs.getLong("idOpcion"),
                rs.getString("textoOpcion"),
                rs.getString("textoPareja"),
                rs.getLong("idTipoRespuesta")
            ));
        });
    
        return new ArrayList<>(preguntasMap.values());
    }
    

    /**
     * Lista las preguntas filtradas por el docente y el tema.
     *
     * @param idDocente identificador del docente
     * @param idTema identificador del tema
     * @return lista de preguntas que pertenecen al docente y al tema
     */
    public List<ObtenerPreguntaDto> listarPreguntasPorDocenteYTema(Long idDocente, Long idTema) {
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
                o.textoPareja,
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
            WHERE u.idUsuario = ? AND t.idTema = ?
            ORDER BY p.idPregunta
        """;
    
        Map<Long, ObtenerPreguntaDto> preguntasMap = new LinkedHashMap<>();
        jdbcTemplate.query(sql, ps -> {
            ps.setLong(1, idDocente);
            ps.setLong(2, idTema);
        }, rs -> {
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
    
            // Agregar opción de respuesta
            pregunta.opciones().add(new ObtenerOpcionRespuestaDto(
                rs.getLong("idOpcion"),
                rs.getString("textoOpcion"),
                rs.getString("textoPareja"),
                rs.getLong("idTipoRespuesta")
            ));
        });
    
        return new ArrayList<>(preguntasMap.values());
    }

}