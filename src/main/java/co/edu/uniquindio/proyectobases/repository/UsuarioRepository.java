package co.edu.uniquindio.proyectobases.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.UsuarioDto.UsuarioDetalleDto;
import co.edu.uniquindio.proyectobases.dto.UsuarioDto.UsuarioDto;
import io.micrometer.common.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UsuarioRepository {
    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UsuarioDto> obtenerTodosUsuarios() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("obtener_usuarios")
                .returningResultSet("p_cursor", new UsuarioRowMapper());

        Map<String, Object> result = jdbcCall.execute();

        @SuppressWarnings("unchecked")
        List<UsuarioDto> usuarios = (List<UsuarioDto>) result.get("p_cursor");

        return usuarios;
    }

    @SuppressWarnings("null")
    private static class UsuarioRowMapper implements RowMapper<UsuarioDto> {
        @Override
        public UsuarioDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Timestamp timestamp = rs.getTimestamp("FECHAREGISTRO");
            LocalDateTime fechaRegistro = timestamp != null ? timestamp.toLocalDateTime() : null;
            int activo = rs.getInt("ACTIVO");

            return new UsuarioDto(
                    rs.getLong("IDUSUARIO"),
                    rs.getString("NOMBRE"),
                    rs.getString("APELLIDO"),
                    rs.getLong("IDUNIDAD"),
                    fechaRegistro,
                    activo == 1
            );
        }
    }


    public Optional<UsuarioDetalleDto> obtenerUsuarioPorId(Long idUsuario) {
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName("obtener_usuario_detalle")
        .declareParameters(
            new SqlParameter("p_idUsuario", Types.NUMERIC),
            new SqlOutParameter("p_nombre", Types.VARCHAR),
            new SqlOutParameter("p_apellido", Types.VARCHAR),
            new SqlOutParameter("p_correo", Types.VARCHAR),
            new SqlOutParameter("p_unidad", Types.VARCHAR),
            new SqlOutParameter("p_fechaRegistro", Types.TIMESTAMP),
            new SqlOutParameter("p_estado", Types.VARCHAR),
            new SqlOutParameter("p_idRol", Types.NUMERIC),
            new SqlOutParameter("p_resultado", Types.INTEGER)
        );

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("p_idUsuario", idUsuario);

        Map<String, Object> result = jdbcCall.execute(params);

        Integer resultado = (Integer) result.get("p_resultado");
        if (resultado != null && resultado == 1) {
            return Optional.of(
                new UsuarioDetalleDto(
                    idUsuario,
                    (String) result.get("p_nombre"),
                    (String) result.get("p_apellido"),
                    (String) result.get("p_correo"),
                    (String) result.get("p_unidad"),
                    ((Timestamp) result.get("p_fechaRegistro")).toLocalDateTime(),
                    (String) result.get("p_estado"),
                    ((Number) result.get("p_idRol")).longValue()
                )
            );
        }

        return Optional.empty();
    }



}