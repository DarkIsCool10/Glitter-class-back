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

/**
 * Repositorio encargado de las operaciones relacionadas con las credenciales de usuario.
 * Utiliza JdbcTemplate para interactuar con la base de datos a través de procedimientos almacenados.
 */
@Repository
public class AutenticacionRepository {

    /**
     * JdbcTemplate para realizar operaciones sobre la base de datos.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor que recibe el JdbcTemplate por inyección de dependencias.
     * @param jdbcTemplate plantilla JDBC para operaciones de base de datos
     */
    public AutenticacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Valida las credenciales de un usuario llamando al procedimiento almacenado 'validar_login'.
     * @param correo correo electrónico del usuario
     * @param contrasena contraseña del usuario
     * @return Optional con LoginResponseDto si la autenticación es exitosa, vacío en caso contrario
     */
    public Optional<LoginResponseDto> validarLogin(String correo, String contrasena) {
        try {
            // Configura el llamado al procedimiento almacenado 'validar_login' y sus parámetros de entrada/salida
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("validar_login")
                .declareParameters(
                    new SqlParameter("p_correo", Types.VARCHAR),
                    new SqlParameter("p_contrasena", Types.VARCHAR),
                    new SqlOutParameter("p_result", Types.INTEGER),
                    new SqlOutParameter("p_idusuario", Types.NUMERIC),
                    new SqlOutParameter("p_idrol", Types.NUMERIC)
                );

            // Prepara los parámetros de entrada para el procedimiento
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_correo", correo)
                .addValue("p_contrasena", contrasena);

            // Ejecuta el procedimiento y obtiene los resultados
            Map<String, Object> result = jdbcCall.execute(params);

            // Recupera el código de resultado para verificar si la autenticación fue exitosa
            Number resultCode = (Number) result.get("p_result");

            // Si el resultado es exitoso, retorna el DTO con la información del usuario
            if (resultCode != null && resultCode.intValue() == 1) {
                Long idUsuario = ((Number) result.get("p_idusuario")).longValue();
                Integer idRol = ((Number) result.get("p_idrol")).intValue();
                return Optional.of(new LoginResponseDto(idUsuario, idRol, correo));
            }

        } catch (Exception e) {
            // Manejo de errores: imprime el mensaje de error en consola
            System.err.println("Error en validarLogin: " + e.getMessage());
        }

        // Retorna vacío si la autenticación falla o ocurre un error
        return Optional.empty();
    }
}
