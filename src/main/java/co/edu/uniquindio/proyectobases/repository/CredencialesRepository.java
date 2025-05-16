package co.edu.uniquindio.proyectobases.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginResponseDto;

import java.sql.Types;
import java.util.Map;
import java.util.Optional;

@Repository
public class CredencialesRepository {

    private final JdbcTemplate jdbcTemplate;

    public CredencialesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<LoginResponseDto> validarLogin(String correo, String contrasena) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("validar_login")
                .declareParameters(
                    new SqlParameter("p_correo", Types.VARCHAR),
                    new SqlParameter("p_contrasena", Types.VARCHAR),
                    new SqlOutParameter("p_result", Types.INTEGER),
                    new SqlOutParameter("p_idusuario", Types.NUMERIC),
                    new SqlOutParameter("p_idrol", Types.NUMERIC)
                );

            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_correo", correo)
                .addValue("p_contrasena", contrasena);

            Map<String, Object> result = jdbcCall.execute(params);

            Number resultCode = (Number) result.get("p_result");

            if (resultCode != null && resultCode.intValue() == 1) {
                Long idUsuario = ((Number) result.get("p_idusuario")).longValue();
                Integer idRol = ((Number) result.get("p_idrol")).intValue();
                return Optional.of(new LoginResponseDto(idUsuario, idRol, correo));
            }

        } catch (Exception e) {
            System.err.println("Error en validarLogin: " + e.getMessage());
        }

        return Optional.empty();
    }
}
