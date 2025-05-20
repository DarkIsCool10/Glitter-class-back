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

import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaCreadaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaConOpcionesDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;

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
    
    public OpcionRespuestaCreadaDto crearOpcionRespuesta(Long idPregunta, OpcionRespuestaDto dto) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("crear_opcion_respuesta")
                .declareParameters(
                        new SqlParameter("p_idPregunta", Types.NUMERIC),
                        new SqlParameter("p_textoOpcion", Types.CLOB),
                        new SqlParameter("p_porcentajeParcial", Types.DOUBLE),
                        new SqlParameter("p_idTipoRespuesta", Types.NUMERIC),
                        new SqlOutParameter("p_resultado", Types.NUMERIC),
                        new SqlOutParameter("p_orden", Types.NUMERIC),
                        new SqlOutParameter("p_idOpcion", Types.NUMERIC)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_idPregunta", idPregunta)
                .addValue("p_textoOpcion", dto.textoOpcion())
                .addValue("p_porcentajeParcial", dto.porcentajeParcial())
                .addValue("p_idTipoRespuesta", dto.idTipoRespuesta());

        Map<String, Object> result = jdbcCall.execute(params);
        int resultado = ((Number) result.get("p_resultado")).intValue();    
        int orden = ((Number) result.get("p_orden")).intValue();
        Long idOpcion = ((Number) result.get("p_idOpcion")).longValue();
        return new OpcionRespuestaCreadaDto(resultado, orden, idOpcion);
    }

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
                o.porcentajeParcial,
                o.orden,
                o.idTipoRespuesta
            FROM Pregunta p
            LEFT JOIN OpcionRespuesta o ON p.idPregunta = o.idPregunta
            ORDER BY p.idPregunta, o.orden
        """;
    
        return jdbcTemplate.query(sql, rs -> {
            Map<Long, PreguntaConOpcionesDto> preguntasMap = new LinkedHashMap<>();
    
            while (rs.next()) {
                Long idPregunta = rs.getLong("idPregunta");
    
                // Asegurar que la pregunta esté en el mapa
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
    
                // Verificar si hay una opción y agregarla
                Long idOpcion = rs.getLong("idOpcion");
                if (!rs.wasNull()) {
                    OpcionRespuestaDto opcion = new OpcionRespuestaDto(
                        idOpcion,
                        rs.getString("textoOpcion"),
                        rs.getDouble("porcentajeParcial"),
                        rs.getInt("orden"),
                        rs.getLong("idTipoRespuesta")
                    );
                    preguntasMap.get(idPregunta).opciones().add(opcion);
                }
            }
    
            return new ArrayList<>(preguntasMap.values());
        });
    }
    
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