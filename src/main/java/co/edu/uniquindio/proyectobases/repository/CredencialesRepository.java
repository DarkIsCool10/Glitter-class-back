package co.edu.uniquindio.proyectobases.repository;

import co.edu.uniquindio.proyectobases.model.Credenciales;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CredencialesRepository {

    private final JdbcTemplate jdbcTemplate;

    public CredencialesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Credenciales> validarLogin(String correo, String contrasena) {
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName("validar_login")
        .declareParameters(
            new SqlParameter("p_correo", Types.VARCHAR),
            new SqlParameter("p_contrasena", Types.VARCHAR),
            new SqlOutParameter("p_result", Types.INTEGER),
            new SqlOutParameter("p_idusuario", Types.INTEGER),
            new SqlOutParameter("p_idrol", Types.INTEGER)
        );

        Map<String, Object> params = new HashMap<>();
        params.put("p_correo", correo);
        params.put("p_contrasena", contrasena);

        Map<String, Object> result = jdbcCall.execute(params);

        Number resultCode = (Number) result.get("p_result");
        if (resultCode != null && resultCode.intValue() == 1) {
            Credenciales cred = new Credenciales();
            cred.setIdUsuario(((Number) result.get("p_idusuario")).longValue());
            cred.setIdRol(((Number) result.get("p_idrol")).longValue());
            cred.setCorreo(correo);
            cred.setContrasena(contrasena);
            return Optional.of(cred);
        }
        return Optional.empty();
    }

}
