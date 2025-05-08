package co.edu.uniquindio.proyectobases.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.UsuarioDto;
import io.micrometer.common.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
}