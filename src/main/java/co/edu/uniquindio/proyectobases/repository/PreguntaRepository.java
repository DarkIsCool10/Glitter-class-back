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
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaConOpcionesDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;

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
    public List<PreguntaConOpcionesDto> obtenerTodasLasPreguntasConOpciones() {
        String sql = """
            SELECT 
                p.idPregunta,
                p.enunciado,
                p.idDocente,
                p.idUnidad,
                p.idVisibilidad,
                o.idOpcion,
                o.textoOpcion,
                o.idTipoRespuesta,
                tr.nombre AS nombreTipoRespuesta
            FROM Pregunta p
            LEFT JOIN OpcionRespuesta o ON p.idPregunta = o.idPregunta
            LEFT JOIN TipoRespuesta tr ON o.idTipoRespuesta = tr.idTipoRespuesta
            ORDER BY p.idPregunta
        """;
    
        return jdbcTemplate.query(sql, rs -> {
            // Utiliza un LinkedHashMap para mantener el orden de inserción de las preguntas
            Map<Long, PreguntaConOpcionesDto> preguntasMap = new LinkedHashMap<>();
    
            while (rs.next()) {
                Long idPregunta = rs.getLong("idPregunta");
    
                // Si la pregunta aún no está en el mapa, la agrega con una lista vacía de opciones
                if (!preguntasMap.containsKey(idPregunta)) {
                    PreguntaConOpcionesDto pregunta = new PreguntaConOpcionesDto(
                        idPregunta,
                        rs.getString("enunciado"),
                        rs.getLong("idDocente"),
                        rs.getLong("idUnidad"),
                        rs.getLong("idVisibilidad"),
                        new ArrayList<>()
                    );
                    preguntasMap.put(idPregunta, pregunta);
                }
    
                // Si hay una opción de respuesta asociada, la agrega a la lista de la pregunta
                Long idOpcion = rs.getLong("idOpcion");
                if (!rs.wasNull()) {
                    ObtenerOpcionRespuestaDto opcion = new ObtenerOpcionRespuestaDto(
                        idOpcion,
                        rs.getString("textoOpcion"),
                        rs.getLong("idTipoRespuesta")
                    );
                    preguntasMap.get(idPregunta).opciones().add(opcion);
                }
            }
    
            // Devuelve la lista de preguntas con sus opciones
            return new ArrayList<>(preguntasMap.values());
        });
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
                eg.nombre AS estado
            FROM Pregunta p
            JOIN Tema t ON p.idTema = t.idTema
            JOIN DificultadPregunta dp ON p.idDificultad = dp.idDificultad
            JOIN Usuario u ON p.idDocente = u.idUsuario
            JOIN TipoPregunta tp ON p.idTipo = tp.idTipo
            JOIN UnidadAcademica ua ON p.idUnidad = ua.idUnidad
            JOIN VisibilidadPregunta v ON p.idVisibilidad = v.idVisibilidad
            JOIN EstadoGeneral eg ON p.idEstado = eg.idEstado
            WHERE p.idDocente = ?
            ORDER BY p.fechaCreacion DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ObtenerPreguntaDto(
            rs.getLong("idPregunta"),
            rs.getString("enunciado"),
            rs.getString("tema"),
            rs.getString("visibilidad"),
            rs.getString("dificultad"),
            rs.getString("docente"),
            rs.getString("unidadAcademica"),
            rs.getString("tipo"),
            rs.getDouble("porcentajeNota"),
            rs.getTimestamp("fechaCreacion").toLocalDateTime(),
            rs.getString("estado")
        ), idDocente);
    }


}