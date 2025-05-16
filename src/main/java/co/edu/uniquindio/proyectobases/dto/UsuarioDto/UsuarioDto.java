package co.edu.uniquindio.proyectobases.dto.UsuarioDto;

import java.time.LocalDateTime;
public record UsuarioDto(
        Long idUsuario,
        String nombre,
        String apellido,
        Long idUnidad,
        LocalDateTime fechaRegistro,
        Boolean activo
) {}